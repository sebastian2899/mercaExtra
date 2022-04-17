import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ItemFacturaVentaService } from '../service/item-factura-venta.service';
import { IItemFacturaVenta, ItemFacturaVenta } from '../item-factura-venta.model';

import { ItemFacturaVentaUpdateComponent } from './item-factura-venta-update.component';

describe('ItemFacturaVenta Management Update Component', () => {
  let comp: ItemFacturaVentaUpdateComponent;
  let fixture: ComponentFixture<ItemFacturaVentaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let itemFacturaVentaService: ItemFacturaVentaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ItemFacturaVentaUpdateComponent],
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
      .overrideTemplate(ItemFacturaVentaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ItemFacturaVentaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    itemFacturaVentaService = TestBed.inject(ItemFacturaVentaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const itemFacturaVenta: IItemFacturaVenta = { id: 456 };

      activatedRoute.data = of({ itemFacturaVenta });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(itemFacturaVenta));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ItemFacturaVenta>>();
      const itemFacturaVenta = { id: 123 };
      jest.spyOn(itemFacturaVentaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemFacturaVenta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: itemFacturaVenta }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(itemFacturaVentaService.update).toHaveBeenCalledWith(itemFacturaVenta);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ItemFacturaVenta>>();
      const itemFacturaVenta = new ItemFacturaVenta();
      jest.spyOn(itemFacturaVentaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemFacturaVenta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: itemFacturaVenta }));
      saveSubject.complete();

      // THEN
      expect(itemFacturaVentaService.create).toHaveBeenCalledWith(itemFacturaVenta);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ItemFacturaVenta>>();
      const itemFacturaVenta = { id: 123 };
      jest.spyOn(itemFacturaVentaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemFacturaVenta });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(itemFacturaVentaService.update).toHaveBeenCalledWith(itemFacturaVenta);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
