import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditJerseyComponent } from './edit-jersey.component';

describe('EditJerseyComponent', () => {
  let component: EditJerseyComponent;
  let fixture: ComponentFixture<EditJerseyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EditJerseyComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditJerseyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
