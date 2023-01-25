import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from './authentication.service';
import { Role, User } from './user';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Tampa Bay Lightning Jerseys';

  constructor(private router: Router, private auth: AuthenticationService) {}

  get isLoggedIn() { return this.auth.user != null; }
  get isAdmin() {  return this.auth.user?.role == Role.ADMIN; }
  get currentUser() { return this.auth.user?.username}
  
  logout() { 
    this.auth.logout();
    this.router.navigate(['/browse'])
  }
}
