import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CategoriaProductoService } from '../service/categoria-producto.service';
import { ICategoriaProducto, CategoriaProducto } from '../categoria-producto.model';

import { CategoriaProductoUpdateComponent } from './categoria-producto-update.component';

describe('CategoriaProducto Management Update Component', () => {
  let comp: CategoriaProductoUpdateComponent;
  let fixture: ComponentFixture<CategoriaProductoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let categoriaProductoService: CategoriaProductoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CategoriaProductoUpdateComponent],
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
      .overrideTemplate(CategoriaProductoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CategoriaProductoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    categoriaProductoService = TestBed.inject(CategoriaProductoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const categoriaProducto: ICategoriaProducto = { id: 456 };

      activatedRoute.data = of({ categoriaProducto });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(categoriaProducto));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CategoriaProducto>>();
      const categoriaProducto = { id: 123 };
      jest.spyOn(categoriaProductoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaProducto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: categoriaProducto }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(categoriaProductoService.update).toHaveBeenCalledWith(categoriaProducto);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CategoriaProducto>>();
      const categoriaProducto = new CategoriaProducto();
      jest.spyOn(categoriaProductoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaProducto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: categoriaProducto }));
      saveSubject.complete();

      // THEN
      expect(categoriaProductoService.create).toHaveBeenCalledWith(categoriaProducto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CategoriaProducto>>();
      const categoriaProducto = { id: 123 };
      jest.spyOn(categoriaProductoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ categoriaProducto });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(categoriaProductoService.update).toHaveBeenCalledWith(categoriaProducto);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
