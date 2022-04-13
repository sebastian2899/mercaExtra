import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CompraService } from '../service/compra.service';
import { ICompra, Compra } from '../compra.model';

import { CompraUpdateComponent } from './compra-update.component';

describe('Compra Management Update Component', () => {
  let comp: CompraUpdateComponent;
  let fixture: ComponentFixture<CompraUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let compraService: CompraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CompraUpdateComponent],
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
      .overrideTemplate(CompraUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompraUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    compraService = TestBed.inject(CompraService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const compra: ICompra = { id: 456 };

      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(compra));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Compra>>();
      const compra = { id: 123 };
      jest.spyOn(compraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compra }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(compraService.update).toHaveBeenCalledWith(compra);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Compra>>();
      const compra = new Compra();
      jest.spyOn(compraService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compra }));
      saveSubject.complete();

      // THEN
      expect(compraService.create).toHaveBeenCalledWith(compra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Compra>>();
      const compra = { id: 123 };
      jest.spyOn(compraService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compra });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(compraService.update).toHaveBeenCalledWith(compra);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
