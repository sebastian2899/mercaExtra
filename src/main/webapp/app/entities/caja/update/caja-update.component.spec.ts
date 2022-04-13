import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CajaService } from '../service/caja.service';
import { ICaja, Caja } from '../caja.model';

import { CajaUpdateComponent } from './caja-update.component';

describe('Caja Management Update Component', () => {
  let comp: CajaUpdateComponent;
  let fixture: ComponentFixture<CajaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cajaService: CajaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CajaUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CajaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CajaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cajaService = TestBed.inject(CajaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const caja: ICaja = { id: 456 };

      activatedRoute.data = of({ caja });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(caja));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Caja>>();
      const caja = { id: 123 };
      jest.spyOn(cajaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ caja });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: caja }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cajaService.update).toHaveBeenCalledWith(caja);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Caja>>();
      const caja = new Caja();
      jest.spyOn(cajaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ caja });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: caja }));
      saveSubject.complete();

      // THEN
      expect(cajaService.create).toHaveBeenCalledWith(caja);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Caja>>();
      const caja = { id: 123 };
      jest.spyOn(cajaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ caja });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cajaService.update).toHaveBeenCalledWith(caja);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
