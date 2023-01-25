import { Component, OnInit } from '@angular/core';
import { Jersey } from '../jersey';
import { JerseyService } from '../jersey.service';

@Component({
  selector: 'app-browse-jerseys',
  templateUrl: './browse-jerseys.component.html',
  styleUrls: [ './browse-jerseys.component.css' ]
})
export class BrowseJerseysComponent implements OnInit {
  jerseys: Jersey[] = [];
  searchTerm: string = ''

  constructor(private jerseyService: JerseyService) {}

  ngOnInit(): void {
    this.jerseyService.getJerseys().subscribe(jerseys => this.jerseys = jerseys);
  }

  outOfStock(jersey: Jersey): Boolean {
    return jersey.quantity < 1;
  }

  hasLowStock(jersey: Jersey): Boolean {
    return jersey.quantity < 4;
  }
}