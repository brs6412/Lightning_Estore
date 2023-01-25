import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BrowseJerseysComponent } from './browse-jerseys.component';

describe('BrowseJerseysComponent', () => {
  let component: BrowseJerseysComponent;
  let fixture: ComponentFixture<BrowseJerseysComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BrowseJerseysComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BrowseJerseysComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
