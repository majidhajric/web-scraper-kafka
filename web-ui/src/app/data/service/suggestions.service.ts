import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Link} from '../schema/link';
import {environment} from '../../../environments/environment';
import {Suggestion} from '../schema/suggestion';

// @ts-ignore
@Injectable({
  providedIn: 'root'
})
export class SuggestionsService {

  readonly API_BASE = 'suggestions'
  readonly API = environment.apiServer + '/' + this.API_BASE;

  constructor(private httpClient: HttpClient) { }

  public getSuggestion(pageURL: string): Observable<Suggestion> {
    const httpParams = new HttpParams().set('pageURL', pageURL);
    return this.httpClient.get<Suggestion>(this.API, {params: httpParams});
  }
}
