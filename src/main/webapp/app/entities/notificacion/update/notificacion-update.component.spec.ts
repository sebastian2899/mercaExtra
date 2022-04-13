import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { NotificacionService } from '../service/notificacion.service';
import { INotificacion, Notificacion } from '../notificacion.model';

import { NotificacionUpdateComponent } from './notificacion-update.component';

describe('Notificacion Management Update Component', () => {
  let comp: NotificacionUpdateComponent;
  let fixture: ComponentFixture<NotificacionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let notificacionService: NotificacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [NotificacionUpdateComponent],
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
      .overrideTemplate(NotificacionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NotificacionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    notificacionService = TestBed.inject(NotificacionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const notificacion: INotificacion = { id: 456 };

      activatedRoute.data = of({ notificacion });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(notificacion));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Notificacion>>();
      const notificacion = { id: 123 };
      jest.spyOn(notificacionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notificacion }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(notificacionService.update).toHaveBeenCalledWith(notificacion);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Notificacion>>();
      const notificacion = new Notificacion();
      jest.spyOn(notificacionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: notificacion }));
      saveSubject.complete();

      // THEN
      expect(notificacionService.create).toHaveBeenCalledWith(notificacion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Notificacion>>();
      const notificacion = { id: 123 };
      jest.spyOn(notificacionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ notificacion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(notificacionService.update).toHaveBeenCalledWith(notificacion);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
