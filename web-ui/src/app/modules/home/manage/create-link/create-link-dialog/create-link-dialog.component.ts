import {AfterViewInit, Component, OnDestroy, OnInit, QueryList, ViewChild, ViewChildren} from '@angular/core';
import {BehaviorSubject, of} from 'rxjs';
import {ChipsMultiSelectComponent} from '../../../../../shared/components/chips-multi-select/chips-multi-select.component';
import {Link} from '../../../../../data/schema/link';
import {SuggestionsService} from '../../../../../data/service/suggestions.service';
import {LinksService} from '../../../../../data/service/links.service';
import {Router} from '@angular/router';
import {catchError, finalize} from 'rxjs/operators';
import {MatDialogRef} from '@angular/material/dialog';
import {Suggestion} from '../../../../../data/schema/suggestion';
import {LinkRequest} from '../../../../../data/schema/link-request';

@Component({
  selector: 'app-create-link-dialog',
  templateUrl: './create-link-dialog.component.html',
  styleUrls: ['./create-link-dialog.component.scss']
})
export class CreateLinkDialogComponent implements OnInit, AfterViewInit, OnDestroy  {

  private loadingSubject = new BehaviorSubject<boolean>(false);
  public loading$ = this.loadingSubject.asObservable();

  @ViewChildren(ChipsMultiSelectComponent) tagsSelectComponents: QueryList<ChipsMultiSelectComponent>;

  keywordsSelect: ChipsMultiSelectComponent;
  tagsSelect: ChipsMultiSelectComponent;

  url = '';
  keywords: string[];
  tags: string[];
  suggestion: Suggestion;
  linkRequest = {} as LinkRequest;
  selection: Set<string> = new Set<string>();

  constructor(private dialogRef: MatDialogRef<CreateLinkDialogComponent>,
              private suggestionsService: SuggestionsService,
              private linksService: LinksService) { }

  ngAfterViewInit(): void {
    const selectComponents = this.tagsSelectComponents.toArray();
    this.keywordsSelect = selectComponents[0];
    this.tagsSelect = selectComponents[1];
    this.keywordsSelect.resetValues();
    this.tagsSelect.resetValues();
  }

  ngOnInit(): void {
  }

  analyse() {
    this.loadingSubject.next(true);
    this.suggestionsService.getSuggestion(this.url)
      .pipe(
        catchError(() => of({} as Suggestion)),
        finalize(() => this.loadingSubject.next(false))
      )
      .subscribe(suggestion => {
        this.suggestion = suggestion;
        this.keywords = suggestion.keywords;
        this.tags = suggestion.tags;
        this.linkRequest.tags = [];
        this.linkRequest.url = suggestion.url;
        this.keywordsSelect.resetValues();
        this.tagsSelect.resetValues();
      });
  }

  tagsSelected(values: string[]) {
    values.forEach(value => this.selection.add(value));
  }


  ngOnDestroy(): void {
    this.loadingSubject.complete();
  }

  save() {
    this.linkRequest.tags.push(...Array.from(this.selection));
    this.linksService.saveLink(this.linkRequest);
    this.close();
  }

  close() {
    this.loadingSubject.complete();
    this.dialogRef.close();
  }
}
