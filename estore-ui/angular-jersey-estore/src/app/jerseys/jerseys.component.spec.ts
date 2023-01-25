import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JerseysComponent } from './jerseys.component';

describe('JerseysComponent', () => {
  let component: JerseysComponent;
  let fixture: ComponentFixture<JerseysComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ JerseysComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JerseysComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
