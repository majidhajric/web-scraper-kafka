import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {SuggestionsService} from '../../../../data/service/suggestions.service';
import {BehaviorSubject} from 'rxjs';
import {Link} from '../../../../data/schema/link';
import {ChipsMultiSelectComponent} from '../../../../shared/components/chips-multi-select/chips-multi-select.component';
import {LinksService} from '../../../../data/service/links.service';

@Component({
  selector: 'app-create-link',
  templateUrl: './create-link.component.html',
  styleUrls: ['./create-link.component.scss']
})
export class CreateLinkComponent implements OnInit, AfterViewInit  {

  @ViewChild(ChipsMultiSelectComponent) suggestionsSelect: ChipsMultiSelectComponent;

  url = '';
  suggestions: string[];
  link: Link;

  constructor(private suggestionsService: SuggestionsService, private linksService: LinksService) { }

  ngAfterViewInit(): void {
    this.suggestionsSelect.resetValues();
    }

  ngOnInit(): void {
  }

  analyse() {
    this.suggestionsService.getSeggestions(this.url)
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
  }
}
