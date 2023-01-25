import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { JerseysComponent } from './jerseys/jerseys.component';
import { BrowseJerseysComponent } from './browse/browse-jerseys.component';
import { JerseyDetailComponent } from './jersey-detail/jersey-detail.component';
import { InventoryComponent } from './inventory/inventory.component';
import { AuthGuardService } from './auth-guard.service';
import { LoginComponent } from './login/login.component';
import { EditJerseyComponent } from './edit-jersey/edit-jersey.component';
import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';
import { AddJerseyComponent } from './add-jersey/add-jersey.component';
import { CheckoutComponent } from './checkout/checkout.component';

const routes: Routes = [
  { path: '', redirectTo: '/browse', pathMatch: 'full' },
  { path: 'browse', component: BrowseJerseysComponent},
  { path: 'detail/:id', component: JerseyDetailComponent },
  { path: 'jerseys', component: JerseysComponent },
  { path: 'inventory', component: InventoryComponent, canActivate: [AuthGuardService] },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: LoginComponent, data: { isRegistration: true } },
  { path: 'edit-jersey/:id', component: EditJerseyComponent },
  { path: 'inventory/add-jersey', component: AddJerseyComponent },
  // { path: 'cart', component: ShoppingCartComponent },
  { path: 'checkout', component: CheckoutComponent},

  { path: 'cart',
        children: [
            { path: '', component: ShoppingCartComponent },
            { path: 'item', component: ShoppingCartComponent },
        ]
    }

];

@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})

export class AppRoutingModule { }