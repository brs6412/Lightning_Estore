import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { Jersey } from './jersey';

@Injectable({
  providedIn: 'root'
})

export class JerseyService {

  private jerseysUrl = 'http://localhost:8080/jerseys';  // URL to web api
  
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient) { }

  getJerseys(): Observable<Jersey[]> {
    return this.http.get<Jersey[]>(this.jerseysUrl)
      .pipe(
        catchError(this.handleError<Jersey[]>('getJerseys', []))
      );
  }

  /**
   * Handle Http operation that failed.
   * Let the app continue.
   *
   * @param operation - name of the operation that failed
   * @param result - optional value to return as the observable result
   */
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: send the error to remote logging infrastructure
      console.error(error); // log to console instead

      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }
  
  getJersey(id: number): Observable<Jersey> {
    const url = `${this.jerseysUrl}/${id}`;
    return this.http.get<Jersey>(url);
  }

  /** PUT: update the jersey on the server */
  updateJersey(jersey: Jersey): Observable<any> {
    return this.http.put(this.jerseysUrl, jersey, this.httpOptions);
  }

  /** POST: add a new jersey to the server */
  addJersey(jersey: Jersey): Observable<Jersey> {
    return this.http.post<Jersey>(this.jerseysUrl, jersey, this.httpOptions);
  }

  /** DELETE: delete the jersey from the server */
  deleteJersey(id: number): Observable<Jersey> {
    const url = `${this.jerseysUrl}/${id}`;

    return this.http.delete<Jersey>(url, this.httpOptions);
  }

  /* GET jerseys which name contains search term */
  searchJerseys(term: string): Observable<Jersey[]> {
    if (!term.trim()) {
      // if not search term, return empty jersey array.
      return of([]);
    }
    return this.http.get<Jersey[]>(`${this.jerseysUrl}/?name=${term}`);
  }

}
