import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Link} from '../schema/link';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LinksService {

  constructor(private httpClient: HttpClient) {
  }

  public getAllItems(): Observable<Link[]> {
    return this.httpClient.get<Link[]>(environment.apiServer);
  }

  public getItem(id: string): Observable<Link> {
    return this.httpClient.get<Link>(environment.apiServer + id);
  }
}
