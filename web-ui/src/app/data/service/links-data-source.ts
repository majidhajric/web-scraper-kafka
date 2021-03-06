import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {Link} from '../schema/link';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {LinksService} from './links.service';
import {catchError, finalize} from 'rxjs/operators';

export class LinksDataSource implements DataSource<Link>{

  private linkSubject = new BehaviorSubject<Link[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  public loading$ = this.loadingSubject.asObservable();

  constructor(private linksService: LinksService) {
  }

  connect(collectionViewer: CollectionViewer): Observable<Link[] | ReadonlyArray<Link>> {
    return this.linkSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.linkSubject.complete();
    this.loadingSubject.complete();
  }

  loadLinks(page: number, size: number) {
    this.loadingSubject.next(true);

    this.linksService.getLinksPage(page, size)
      .pipe(
        catchError(() => of([])),
        finalize(() => this.loadingSubject.next(false))
      )
      .subscribe(links => this.linkSubject.next(links));
  }
}
