import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';

import { Jersey } from '../jersey';
import { JerseyService } from '../jersey.service';
import { AuthenticationService } from '../authentication.service';
import { Role } from '../user';
import { CartService } from '../cart.service';
import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';

@Component({
  selector: 'app-jersey-detail',
  templateUrl: './jersey-detail.component.html',
  styleUrls: ['./jersey-detail.component.css']
})
export class JerseyDetailComponent implements OnInit {
  jersey : Jersey | undefined;

  constructor(
    private route: ActivatedRoute,
    private jerseyService: JerseyService,
    private cartService: CartService,
    private location: Location,
    private auth: AuthenticationService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.getJersey();
  }

  getJersey(): void {
    const number = parseInt(this.route.snapshot.paramMap.get('id')!, 10);
    this.jerseyService.getJersey(number)
      .subscribe(jersey => this.jersey = jersey);
  }

  get canAddToCart(): boolean {
    return (this.auth.user != null) && (this.auth.user.role != Role.ADMIN);
  }

  addToCart(): void {
    if (!this.jersey) return;
    this.cartService.addToCart(this.jersey)
      .subscribe({
        next: () => {
          this.router.navigate(['/cart'])
        },
        error: (error: HttpErrorResponse) => {
          switch (error.status) {
            case HttpStatusCode.NotFound:
                alert('Jersey is no longer for sale.');
                break;
            case HttpStatusCode.BadRequest:
                alert('Invalid quantity specified.');
                break;
            case HttpStatusCode.Conflict:
                alert('Jersey is out of stock.');
                break;
            case HttpStatusCode.InternalServerError:
                alert('An error occurred on origin server.');
                break;
            default:
                alert('An unknown error occurred.');
                break;
          }
        }
      });
  }

  goBack(): void {
    this.location.back();
  }

  save(): void {
    if (this.jersey) {
      this.jerseyService.updateJersey(this.jersey)
        .subscribe(() => this.goBack());
    }
  }

}
