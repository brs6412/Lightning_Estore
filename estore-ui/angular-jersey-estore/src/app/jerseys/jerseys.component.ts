import { Component, OnInit } from '@angular/core';
import { Jersey } from '../jersey';
import { JerseyService } from '../jersey.service';

@Component({
  selector: 'app-jerseys',
  templateUrl: './jerseys.component.html',
  styleUrls: ['./jerseys.component.css']
})
export class JerseysComponent implements OnInit {
  jerseys: Jersey[] = []

  constructor(private jerseyService: JerseyService) { }

  ngOnInit(): void {
    this.getJerseys()
  }

  getJerseys(): void {
    this.jerseyService.getJerseys()
    .subscribe(jerseys => this.jerseys = jerseys)
  }

}
