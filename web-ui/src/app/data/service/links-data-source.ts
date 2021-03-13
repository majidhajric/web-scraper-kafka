import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {Link} from '../schema/link';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {LinksService} from './links.service';
import {catchError, finalize} from 'rxjs/operators';
import {Page} from '../schema/page';

export class LinksDataSource implements DataSource<Link>{

  private linkSubject = new BehaviorSubject<Link[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  public loading$: Observable<boolean>;
  private countSubject = new BehaviorSubject<number>(0);
  public counter$ = this.countSubject.asObservable();

  constructor(private linksService: LinksService) {
    this.loading$ = this.loadingSubject.asObservable();
  }

  connect(collectionViewer: CollectionViewer): Observable<Link[] | ReadonlyArray<Link>> {
    return this.linkSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.linkSubject.complete();
    this.loadingSubject.complete();
    this.countSubject.complete();
  }

  loadLinks(page: number, size: number) {
    this.loadingSubject.next(true);

    this.linksService.getLinksPage(page, size)
      .pipe(
        finalize(() => {
          this.loadingSubject.next(false);
        })
      )
      .subscribe((result: Page<Link>) => {
        this.linkSubject.next(result.content);
        this.countSubject.next(result.totalElements);
      });
  }
}
