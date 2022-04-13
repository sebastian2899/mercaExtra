import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EgresoService } from '../service/egreso.service';
import { IEgreso, Egreso } from '../egreso.model';

import { EgresoUpdateComponent } from './egreso-update.component';

describe('Egreso Management Update Component', () => {
  let comp: EgresoUpdateComponent;
  let fixture: ComponentFixture<EgresoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let egresoService: EgresoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EgresoUpdateComponent],
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
      .overrideTemplate(EgresoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EgresoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    egresoService = TestBed.inject(EgresoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const egreso: IEgreso = { id: 456 };

      activatedRoute.data = of({ egreso });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(egreso));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Egreso>>();
      const egreso = { id: 123 };
      jest.spyOn(egresoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egreso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: egreso }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(egresoService.update).toHaveBeenCalledWith(egreso);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Egreso>>();
      const egreso = new Egreso();
      jest.spyOn(egresoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egreso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: egreso }));
      saveSubject.complete();

      // THEN
      expect(egresoService.create).toHaveBeenCalledWith(egreso);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Egreso>>();
      const egreso = { id: 123 };
      jest.spyOn(egresoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egreso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(egresoService.update).toHaveBeenCalledWith(egreso);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
