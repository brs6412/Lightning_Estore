import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddJerseyComponent } from './add-jersey.component';

describe('AddJerseyComponent', () => {
  let component: AddJerseyComponent;
  let fixture: ComponentFixture<AddJerseyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddJerseyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddJerseyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
