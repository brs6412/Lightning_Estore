import { HttpClient, HttpErrorResponse, HttpStatusCode } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ShoppingCart } from '../cart';
import { CartService } from '../cart.service';
import { Jersey } from '../jersey';

@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.css']
})
export class ShoppingCartComponent implements OnInit {
  endpoint: string = 'http://localhost:8080/users/me/cart';
  cart: ShoppingCart = { items: [], total: 0.0 }

  constructor(
    private http: HttpClient,
    private cartService: CartService,
  ) {}

  ngOnInit(): void { this.getCart(); }
  getCart(): void {
    this.http.get<ShoppingCart>(this.endpoint)
      .subscribe(cart => this.cart = cart);
  }
  clearCart(): void {
    this.http.put<any>(this.endpoint, {}).subscribe(cart => this.cart = cart);
  }
  confirmation(): void {
    alert("Thank you for your purchase!");
  }
  canCheckout(): boolean {
    if(this.cart.items.length > 0){
      return true;
    }
    return false;
  }

  increaseItemQuantity(jersey:Jersey, currentQuantity:string): void {
    var newQuantity = Number(currentQuantity);
    newQuantity++;
    this.setItemQuantity(jersey, String(newQuantity));
  }

  decreaseItemQuantity(jersey:Jersey, currentQuantity:string): void {
    if (Number(currentQuantity) == 1 && confirm("Remove from cart?")){
      this.removeFromCart(jersey);
      return
    }
    var newQuantity = Number(currentQuantity);
    newQuantity--;
    this.setItemQuantity(jersey, String(newQuantity));
  }

  setItemQuantity(jersey:Jersey, inputQuantity:string) {
    var quantity = Number(inputQuantity)
    this.cartService.setQuantity(this.cart.items.find(item => item.jersey === jersey)?.jersey!, quantity)
    .subscribe({
        next: (cart) => {
          this.cart = cart
        },
        error: (error: HttpErrorResponse) => {
          switch (error.status) {
            case HttpStatusCode.Conflict:
                alert('Only ' + error.error + ' ' + jersey.name + ' jerseys are in stock.');
                break;
            case HttpStatusCode.InternalServerError:
                alert('An error occurred on origin server.');
                break;
            default:
                break;
          }
        }
    });
  }

  removeFromCart(jersey: Jersey): void {
    console.log("This is working!")
    const id = jersey.id;
    this.http.delete<any>(this.endpoint, {
      body: {
        jersey: id
      }
  }).subscribe(cart => this.cart = cart);
}

}
