import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EgresoDetailComponent } from './egreso-detail.component';

describe('Egreso Management Detail Component', () => {
  let comp: EgresoDetailComponent;
  let fixture: ComponentFixture<EgresoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EgresoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ egreso: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EgresoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EgresoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load egreso on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.egreso).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
