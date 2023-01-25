import { Component, OnInit } from '@angular/core';

import { Jersey } from '../jersey';
import { JerseyService } from '../jersey.service';
import { JerseysComponent } from '../jerseys/jerseys.component';
import { InventoryComponent } from '../inventory/inventory.component';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { HttpErrorResponse, HttpStatusCode } from '@angular/common/http';

@Component({
  selector: 'app-edit-jersey',
  templateUrl: './edit-jersey.component.html',
  styleUrls: ['./edit-jersey.component.css']
})

export class EditJerseyComponent implements OnInit {
  jersey: Jersey | undefined;

  constructor(
    private route: ActivatedRoute,
    private jerseyService: JerseyService,
    private location: Location
  ) { }

  ngOnInit(): void {
    this.getJersey()
  }

  getJersey(): void {
    var id = parseInt(this.route.snapshot.paramMap.get('id')!, 10)
    this.jerseyService.getJersey(id).subscribe(jersey => this.jersey = jersey)
  }

  goBack(): void {
    this.location.back();
  }


  // save(): void {
  //   if(this.jersey){
  //     this.jerseyService.updateJersey(this.jersey).subscribe(() => {
  //       this.goBack(); 
  //       alert("Item Updated Successfully!");
  //     });
  //   }
  // }

  save(): void {
    if(this.jersey){
      this.jerseyService.updateJersey(this.jersey)
      .subscribe(
        {
          next: () => { 
            this.goBack();
            alert("Item updated successfully!");
        },
          error: (error: HttpErrorResponse) => {
            switch (error.status) {
              case HttpStatusCode.Conflict:
                alert("Failed to update item. Jersey:"
                  + "\n\tName: " + this.jersey?.name
                  + "\n\tNumber: " + this.jersey?.number
                  + "\n\tPrice: " + this.jersey?.price
                  + "\n\tSize: " + this.jersey?.size
                  + "\n\tColor: " + this.jersey?.color
                  + "\nAlready exists.");
                  break;
              default:
                alert("Failed to update item. Invalid details:"
                + "\n\tName: " + this.jersey?.name
                + "\n\tNumber: " + this.jersey?.number
                + "\n\tPrice: " + this.jersey?.price
                + "\n\tSize: " + this.jersey?.size
                + "\n\tColor: " + this.jersey?.color);
                  break;
            }
        }
      });
    }
  }

}

