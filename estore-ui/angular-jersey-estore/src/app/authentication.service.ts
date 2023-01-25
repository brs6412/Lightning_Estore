import { HttpClient, HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, catchError, map, Observable, of } from 'rxjs';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  endpoint: string = 'http://localhost:8080';
  user: User | null;

  constructor(private http: HttpClient, private router: Router) { 
    this.user = null;
    const token = localStorage.getItem('token');
    if (token) {
      this.getUserProfile().subscribe(res => {
        this.user = res;
      })
    }
  }

  getUserProfile(): Observable<User> {
    return this.http.get<User>(`${this.endpoint}/users/me`).pipe(
      map(res => res || null),
      catchError((error: HttpErrorResponse) => {
        localStorage.removeItem('token');
        return of();
      })
    )
  }

  login(username: string, password: string) {
    return this.#authenticate(username, password, false);
  }

  register(username: string, password: string) {
    return this.#authenticate(username, password, true);
  }

  #authenticate(username: string, password: string, shouldRegister: boolean) {
    const url = `http://localhost:8080/auth/${(shouldRegister) ? 'register' : 'login'}`;
    return this.http
      .post<any>(url, { username, password })
      .pipe(
        map(user => {
          localStorage.setItem('token', user.token);
          this.user = ({
            username: user.username,
            role: user.role
          });
          this.router.navigate(['/browse'])
          return HttpStatusCode.Ok;
        }),
        catchError((error: HttpErrorResponse) => {
          return of(error.status);
        })
      );
  }

  logout() {
    localStorage.removeItem('token');
    this.user = null;
  }
}
