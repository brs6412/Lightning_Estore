import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from '../authentication.service';

import { Jersey } from '../jersey';
import { JerseyService } from '../jersey.service';

@Component({
  selector: 'app-inventory',
  templateUrl: './inventory.component.html',
  styleUrls: ['./inventory.component.css']
})
export class InventoryComponent implements OnInit {
  jerseys: Jersey[] = []

  constructor(
    private jerseyService: JerseyService,
    private auth: AuthenticationService,
    private router: Router
  ) { 
    if (!this.auth.user) this.router.navigate(['/browse']);
  }

  ngOnInit(): void {
    this.getJerseys()
  }

  getJerseys(): void {
    this.jerseyService.getJerseys()
    .subscribe(jerseys => this.jerseys = jerseys)
  }

  delete(jersey: Jersey): void {
    if (confirm("Are you sure you want to delete Jersey with:"
    + "\n\tName: " + jersey.name
    + "\n\tNumber: " + jersey.number
    + "\n\tPrice: " + jersey.price
    + "\n\tSize: " + jersey.size
    + "\n\tColor: " + jersey.color
    ))  
    {
      this.jerseys = this.jerseys.filter(j => j !== jersey);
      this.jerseyService.deleteJersey(jersey.id).subscribe();
    }
  }

}
