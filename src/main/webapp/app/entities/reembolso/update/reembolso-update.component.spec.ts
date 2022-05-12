import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ReembolsoService } from '../service/reembolso.service';
import { IReembolso, Reembolso } from '../reembolso.model';

import { ReembolsoUpdateComponent } from './reembolso-update.component';

describe('Reembolso Management Update Component', () => {
  let comp: ReembolsoUpdateComponent;
  let fixture: ComponentFixture<ReembolsoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reembolsoService: ReembolsoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ReembolsoUpdateComponent],
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
      .overrideTemplate(ReembolsoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReembolsoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reembolsoService = TestBed.inject(ReembolsoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const reembolso: IReembolso = { id: 456 };

      activatedRoute.data = of({ reembolso });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(reembolso));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Reembolso>>();
      const reembolso = { id: 123 };
      jest.spyOn(reembolsoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reembolso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reembolso }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(reembolsoService.update).toHaveBeenCalledWith(reembolso);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Reembolso>>();
      const reembolso = new Reembolso();
      jest.spyOn(reembolsoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reembolso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reembolso }));
      saveSubject.complete();

      // THEN
      expect(reembolsoService.create).toHaveBeenCalledWith(reembolso);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Reembolso>>();
      const reembolso = { id: 123 };
      jest.spyOn(reembolsoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reembolso });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reembolsoService.update).toHaveBeenCalledWith(reembolso);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
