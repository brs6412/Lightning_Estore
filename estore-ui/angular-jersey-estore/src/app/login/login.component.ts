import { HttpStatusCode } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { first, map } from 'rxjs';
import { AuthenticationService } from '../authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  myLoginForm: FormGroup;
  error?: string;
  isRegistration: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private auth: AuthenticationService
  ) { 
    if (this.auth.user) this.router.navigate(['/browse']);

    route.data.subscribe(data => {
      this.isRegistration = data['isRegistration'] ?? false;
    });

    this.myLoginForm = this.formBuilder.group({
      username: ['', {
        validators: [Validators.required, Validators.minLength(3)],
        asyncValidators: [],
        updateOn: 'submit'
      }],
      password: ['', {
        validators: [Validators.required],
        asyncValidators: [],
        updateOn: 'submit'
      }]
    });
  }

  get username() { return this.myLoginForm.get('username'); }
  get password() { return this.myLoginForm.get('password'); }

  ngOnInit(): void {}

  onSubmit() {
    if (this.isRegistration)
      this.onSubmitRegister();
    else
      this.onSubmitLogin();
  }

  onSubmitLogin() {
    if (this.myLoginForm.invalid) return;
    this.auth.login(this.username?.value, this.password?.value).subscribe(value => {
      this.error = undefined;
      if (value == HttpStatusCode.Forbidden)
        this.error = 'The username or password was incorrect!';
      else if (value != HttpStatusCode.Ok)
        this.error = 'An unknown error occurred!';
    });
  }

  onSubmitRegister() {
    if (this.myLoginForm.invalid) return;
    this.auth.register(this.username?.value, this.password?.value).subscribe(value => {
      this.error = undefined;
      if (value == HttpStatusCode.Conflict)
        this.error = 'This username already exists!';
      else if (value == HttpStatusCode.BadRequest)
        this.error = 'The username or password is invalid!';
      else if (value != HttpStatusCode.Created)
        this.error = 'An unknown error occurred!';
    });
  }

}
