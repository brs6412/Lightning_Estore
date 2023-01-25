import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { Jersey } from './jersey';
import { ShoppingCart } from './cart';

@Injectable({
  providedIn: 'root'
})

export class CartService {

  private cartUrl = `http://localhost:8080/users/me/cart`;
  private cartItemUrl = `http://localhost:8080/users/me/cart/item`;
  
  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(
    private http: HttpClient) { }

  addToCart(jersey: Jersey): Observable<ShoppingCart> {
    const id = jersey.id;
    return this.http.post<any>(this.cartUrl, {
      jersey: id,
      quantity: 1
    });
  }

  removeFromCart(jersey: Jersey): Observable<ShoppingCart> {
    const id = jersey.id;
    return this.http.post<any>(this.cartUrl, {
      jersey: id,
      quantity: 1
    });
  }

  setQuantity(jersey: Jersey, quantity:Number): Observable<ShoppingCart> {
    const id = jersey.id;
    return this.http.put<any>(this.cartItemUrl, {
      jersey: id,
      quantity: quantity
    }, this.httpOptions);
  }

}
