import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JerseyDetailComponent } from './jersey-detail.component';

describe('JerseyDetailComponent', () => {
  let component: JerseyDetailComponent;
  let fixture: ComponentFixture<JerseyDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JerseyDetailComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JerseyDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
