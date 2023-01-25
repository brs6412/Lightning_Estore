import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';

import { AuthenticationService } from '../authentication.service';
import { Role } from '../user';
import { HttpClient } from '@angular/common/http';
import { ShoppingCart } from '../cart';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  endpoint: string = 'http://localhost:8080/users/me/cart';
  cart: ShoppingCart = { items: [], total: 0.0 }

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private auth: AuthenticationService,
    private http: HttpClient,
    private router: Router
    ) { }

  ngOnInit(): void { this.getCart(); }

  getCart(): void {
    this.http.get<ShoppingCart>(this.endpoint)
      .subscribe(cart => this.cart = cart);
  }

  goBack(): void {
    this.location.back();
  }

}
