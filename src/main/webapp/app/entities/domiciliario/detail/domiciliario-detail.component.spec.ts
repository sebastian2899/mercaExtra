import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DomiciliarioDetailComponent } from './domiciliario-detail.component';

describe('Domiciliario Management Detail Component', () => {
  let comp: DomiciliarioDetailComponent;
  let fixture: ComponentFixture<DomiciliarioDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DomiciliarioDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ domiciliario: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DomiciliarioDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DomiciliarioDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load domiciliario on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.domiciliario).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
