import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DomiciliarioService } from '../service/domiciliario.service';
import { IDomiciliario, Domiciliario } from '../domiciliario.model';

import { DomiciliarioUpdateComponent } from './domiciliario-update.component';

describe('Domiciliario Management Update Component', () => {
  let comp: DomiciliarioUpdateComponent;
  let fixture: ComponentFixture<DomiciliarioUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let domiciliarioService: DomiciliarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DomiciliarioUpdateComponent],
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
      .overrideTemplate(DomiciliarioUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DomiciliarioUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    domiciliarioService = TestBed.inject(DomiciliarioService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const domiciliario: IDomiciliario = { id: 456 };

      activatedRoute.data = of({ domiciliario });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(domiciliario));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Domiciliario>>();
      const domiciliario = { id: 123 };
      jest.spyOn(domiciliarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ domiciliario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: domiciliario }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(domiciliarioService.update).toHaveBeenCalledWith(domiciliario);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Domiciliario>>();
      const domiciliario = new Domiciliario();
      jest.spyOn(domiciliarioService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ domiciliario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: domiciliario }));
      saveSubject.complete();

      // THEN
      expect(domiciliarioService.create).toHaveBeenCalledWith(domiciliario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Domiciliario>>();
      const domiciliario = { id: 123 };
      jest.spyOn(domiciliarioService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ domiciliario });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(domiciliarioService.update).toHaveBeenCalledWith(domiciliario);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
