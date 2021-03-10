import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {BehaviorSubject, of} from 'rxjs';
import {ChipsMultiSelectComponent} from '../../../../../shared/components/chips-multi-select/chips-multi-select.component';
import {Link} from '../../../../../data/schema/link';
import {SuggestionsService} from '../../../../../data/service/suggestions.service';
import {LinksService} from '../../../../../data/service/links.service';
import {Router} from '@angular/router';
import {catchError, finalize} from 'rxjs/operators';
import {MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-create-link-dialog',
  templateUrl: './create-link-dialog.component.html',
  styleUrls: ['./create-link-dialog.component.scss']
})
export class CreateLinkDialogComponent implements OnInit, AfterViewInit, OnDestroy  {

  private loadingSubject = new BehaviorSubject<boolean>(false);
  public loading$ = this.loadingSubject.asObservable();

  @ViewChild(ChipsMultiSelectComponent) suggestionsSelect: ChipsMultiSelectComponent;

  url = '';
  suggestions: string[];
  link: Link;

  constructor(private dialogRef: MatDialogRef<CreateLinkDialogComponent>,
              private suggestionsService: SuggestionsService,
              private linksService: LinksService) { }

  ngAfterViewInit(): void {
    this.suggestionsSelect.resetValues();
  }

  ngOnInit(): void {
  }

  analyse() {
    this.loadingSubject.next(true);
    this.suggestionsService.getSeggestions(this.url)
      .pipe(
        catchError(() => of({} as Link)),
        finalize(() => this.loadingSubject.next(false))
      )
      .subscribe(link => {
        this.suggestions = link.tags;
        this.link = link;
        this.link.tags = [];
        this.suggestionsSelect.resetValues();
      });
  }

  tagsSelected(values: string[]) {
    if (this.link) {
      this.link.tags = [];
      this.link.tags.push(...values);
    }
  }


  ngOnDestroy(): void {
    this.loadingSubject.complete();
  }

  save() {
    this.linksService.saveLink(this.link);
    this.close();
  }

  close() {
    this.loadingSubject.complete();
    this.dialogRef.close();
  }
}
