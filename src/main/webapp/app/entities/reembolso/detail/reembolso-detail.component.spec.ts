import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReembolsoDetailComponent } from './reembolso-detail.component';

describe('Reembolso Management Detail Component', () => {
  let comp: ReembolsoDetailComponent;
  let fixture: ComponentFixture<ReembolsoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReembolsoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ reembolso: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ReembolsoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ReembolsoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load reembolso on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.reembolso).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
