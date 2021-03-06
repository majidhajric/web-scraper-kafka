import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Link} from '../schema/link';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SuggestionsService {

  constructor(private httpClient: HttpClient) { }

  public getSeggestions(pageURL: string): Observable<Link> {
    let params: HttpParams = new HttpParams();
    params.set('pageURL', pageURL);
    return this.httpClient.get<Link>(environment.apiServer + '/suggestions');
  }
}
