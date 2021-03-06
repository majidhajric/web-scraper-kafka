import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {LinksDataSource} from '../../../../data/service/links-data-source';
import {LinksService} from '../../../../data/service/links.service';
import {MatPaginator} from '@angular/material/paginator';
import {tap} from 'rxjs/operators';

@Component({
  selector: 'app-link-list',
  templateUrl: './link-list.component.html',
  styleUrls: ['./link-list.component.scss']
})
export class LinkListComponent implements OnInit {

  totalLinks = 10;
  dataSource: LinksDataSource;
  displayedColumns = ['Link'];

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private linksService: LinksService) { }

  ngOnInit(): void {
    this.dataSource = new LinksDataSource(this.linksService);
    this.dataSource.loadLinks(0, 5);
  }


  onRowClicked(row: any) {
    console.log('Row clicked: ', row);
  }
}
