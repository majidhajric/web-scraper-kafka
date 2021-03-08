import {Component, Input, OnInit} from '@angular/core';
import {Link} from '../../../../data/schema/link';

@Component({
  selector: 'app-link-view',
  templateUrl: './link-view.component.html',
  styleUrls: ['./link-view.component.scss']
})
export class LinkViewComponent implements OnInit {

  @Input() link: Link;
  tagsText: string;

  constructor() { }

  ngOnInit(): void {
    this.tagsText = this.link.tags.join(', ') + '.';
  }

}
