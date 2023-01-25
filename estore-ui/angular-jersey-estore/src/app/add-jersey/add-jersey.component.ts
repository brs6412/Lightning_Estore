import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../authentication.service';
import { Location } from '@angular/common';

import { Jersey } from '../jersey';
import { JerseyService } from '../jersey.service';
import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';

@Component({
  selector: 'app-add-jersey',
  templateUrl: './add-jersey.component.html',
  styleUrls: ['./add-jersey.component.css']
})
export class AddJerseyComponent implements OnInit {
  jerseys: Jersey[] = []

  constructor(
    private jerseyService: JerseyService,
    private auth: AuthenticationService,
    private router: Router,
    private location: Location,
  ) { 
    if (!this.auth.user) this.router.navigate(['/browse']);
  }

  ngOnInit(): void {
    this.getJerseys()
  }

  add(inputName: string, inputNum: string, inputPrice: string, size: string, color: string, inputQuantity: string): void {
    var name = inputName.trim();
    var number = Number(inputNum);
    var price = Number(inputPrice);
    var quantity = Number(inputQuantity);
    if (!name) { return; }
  
    this.jerseyService.addJersey({ name, number, price, size, color, quantity } as Jersey)
      .subscribe(
        {
          next: (jersey: Jersey) => { 
            this.jerseys.push(jersey);
            alert("Item added successfully!");
        },
        error: (error: HttpErrorResponse) => {
          switch (error.status) {
            case HttpStatusCode.Conflict:
                alert("Failed to add item. Jersey:"
                  + "\n\tName: " + name
                  + "\n\tNumber: " + number
                  + "\n\tPrice: " + price
                  + "\n\tSize: " + size
                  + "\n\tColor: " + color
                  + "\nAlready exists."
                )
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

  getJerseys(): void {
    this.jerseyService.getJerseys()
    .subscribe(jerseys => this.jerseys = jerseys)
  }

  goBack(): void {
    this.location.back();
  }

}