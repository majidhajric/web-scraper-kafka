import {AfterViewInit, Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {SuggestionsService} from '../../../../data/service/suggestions.service';
import {BehaviorSubject, of} from 'rxjs';
import {Link} from '../../../../data/schema/link';
import {ChipsMultiSelectComponent} from '../../../../shared/components/chips-multi-select/chips-multi-select.component';
import {LinksService} from '../../../../data/service/links.service';
import {catchError, finalize} from 'rxjs/operators';
import {Page} from '../../../../data/schema/page';
import {Router} from '@angular/router';

@Component({
  selector: 'app-create-link',
  templateUrl: './create-link.component.html',
  styleUrls: ['./create-link.component.scss']
})
export class CreateLinkComponent implements OnInit, AfterViewInit, OnDestroy  {

  private loadingSubject = new BehaviorSubject<boolean>(false);
  public loading$ = this.loadingSubject.asObservable();

  @ViewChild(ChipsMultiSelectComponent) suggestionsSelect: ChipsMultiSelectComponent;

  url = '';
  suggestions: string[];
  link: Link;

  constructor(private suggestionsService: SuggestionsService, private linksService: LinksService, private router: Router) { }

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

  save() {
    this.linksService.saveLink(this.link);
    this.router.navigate(['home']);
  }

  ngOnDestroy(): void {
    this.loadingSubject.complete();
  }
}
