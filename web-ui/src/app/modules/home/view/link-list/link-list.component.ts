import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {LinksDataSource} from '../../../../data/service/links-data-source';
import {LinksService} from '../../../../data/service/links.service';
import {MatPaginator} from '@angular/material/paginator';
import {tap} from 'rxjs/operators';
import {Link} from '../../../../data/schema/link';
import {Page} from '../../../../data/schema/page';

@Component({
  selector: 'app-link-list',
  templateUrl: './link-list.component.html',
  styleUrls: ['./link-list.component.scss']
})
export class LinkListComponent implements OnInit, AfterViewInit {

  page: Page<Link>;
  dataSource: LinksDataSource;
  displayedColumns = ['Link'];

  filter: string;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private linksService: LinksService) { }

  ngOnInit(): void {
    this.dataSource = new LinksDataSource(this.linksService);
    this.dataSource.loadLinks(0, 5);
     }

  ngAfterViewInit() {
    this.dataSource.counter$
      .pipe(
        tap((count) => {
          this.paginator.length = count;
        })
      )
      .subscribe();

    this.paginator.page
      .pipe(
        tap(() => this.loadLinks())
      )
      .subscribe();
  }


  onRowClicked(row: any) {
    console.log('Row clicked: ', row);
  }

  private loadLinks() {
    this.dataSource.loadLinks(this.paginator.pageIndex, this.paginator.pageSize);
  }
}
