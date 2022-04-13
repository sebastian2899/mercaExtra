import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CajaDetailComponent } from './caja-detail.component';

describe('Caja Management Detail Component', () => {
  let comp: CajaDetailComponent;
  let fixture: ComponentFixture<CajaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CajaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ caja: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CajaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CajaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load caja on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.caja).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
