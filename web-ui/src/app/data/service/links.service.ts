import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Link} from '../schema/link';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LinksService {

  readonly API_BASE = 'links';
  readonly API = environment.apiServer + '/' + this.API_BASE;

  constructor(private httpClient: HttpClient) {
  }

  public getLinksPage(page = 0, size = 5): Observable<Link[]> {
    return this.httpClient.get<Link[]>(this.API + '/all',
      {
      params: new HttpParams()
        .set('page', page.toString())
        .set('size', size.toString())
    });
  }

  public getItem(id: string): Observable<Link> {
    return this.httpClient.get<Link>(this.API + '/' + id);
  }

  public saveLink(link: Link) {
    return this.httpClient.post<any>(this.API, link)
  .subscribe();
  }
}
