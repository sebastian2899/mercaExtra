import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { INotificacion, Notificacion } from '../notificacion.model';
import { NotificacionService } from '../service/notificacion.service';

@Component({
  selector: 'jhi-notificacion-update',
  templateUrl: './notificacion-update.component.html',
})
export class NotificacionUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    descripcion: [],
    fecha: [],
  });

  constructor(protected notificacionService: NotificacionService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notificacion }) => {
      if (notificacion.id === undefined) {
        const today = dayjs().startOf('day');
        notificacion.fecha = today;
      }

      this.updateForm(notificacion);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notificacion = this.createFromForm();
    if (notificacion.id !== undefined) {
      this.subscribeToSaveResponse(this.notificacionService.update(notificacion));
    } else {
      this.subscribeToSaveResponse(this.notificacionService.create(notificacion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotificacion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(notificacion: INotificacion): void {
    this.editForm.patchValue({
      id: notificacion.id,
      descripcion: notificacion.descripcion,
      fecha: notificacion.fecha ? notificacion.fecha.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): INotificacion {
    return {
      ...new Notificacion(),
      id: this.editForm.get(['id'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      fecha: this.editForm.get(['fecha'])!.value ? dayjs(this.editForm.get(['fecha'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
