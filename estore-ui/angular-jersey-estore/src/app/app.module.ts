import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppComponent } from './app.component';
import { BrowseJerseysComponent } from './browse/browse-jerseys.component';
import { JerseysComponent } from './jerseys/jerseys.component';
import { JerseyDetailComponent } from './jersey-detail/jersey-detail.component';

import { AppRoutingModule } from './app-routing.module';
import { InventoryComponent } from './inventory/inventory.component';
import { LoginComponent } from './login/login.component';
import { EditJerseyComponent } from './edit-jersey/edit-jersey.component';
import { AddJerseyComponent } from './add-jersey/add-jersey.component';
import { AuthInterceptor } from './_helpers/auth.interceptor';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { CheckoutComponent } from './checkout/checkout.component';
import { SearchPipe } from './_helpers/search.pipe';

@NgModule({
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  declarations: [
    SearchPipe,
    AppComponent,
    BrowseJerseysComponent,
    JerseysComponent,
    JerseyDetailComponent,
    InventoryComponent,
    LoginComponent,
    AddJerseyComponent,
    EditJerseyComponent,
    ShoppingCartComponent,
    CheckoutComponent
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
