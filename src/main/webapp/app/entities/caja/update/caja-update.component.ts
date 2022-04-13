import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICaja, Caja } from '../caja.model';
import { CajaService } from '../service/caja.service';

@Component({
  selector: 'jhi-caja-update',
  templateUrl: './caja-update.component.html',
})
export class CajaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fechaCreacion: [],
    valorTotalDia: [],
    valorRegistradoDia: [],
    diferencia: [],
    estado: [],
  });

  constructor(protected cajaService: CajaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ caja }) => {
      if (caja.id === undefined) {
        const today = dayjs().startOf('day');
        caja.fechaCreacion = today;
      }

      this.updateForm(caja);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const caja = this.createFromForm();
    if (caja.id !== undefined) {
      this.subscribeToSaveResponse(this.cajaService.update(caja));
    } else {
      this.subscribeToSaveResponse(this.cajaService.create(caja));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICaja>>): void {
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

  protected updateForm(caja: ICaja): void {
    this.editForm.patchValue({
      id: caja.id,
      fechaCreacion: caja.fechaCreacion ? caja.fechaCreacion.format(DATE_TIME_FORMAT) : null,
      valorTotalDia: caja.valorTotalDia,
      valorRegistradoDia: caja.valorRegistradoDia,
      diferencia: caja.diferencia,
      estado: caja.estado,
    });
  }

  protected createFromForm(): ICaja {
    return {
      ...new Caja(),
      id: this.editForm.get(['id'])!.value,
      fechaCreacion: this.editForm.get(['fechaCreacion'])!.value
        ? dayjs(this.editForm.get(['fechaCreacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      valorTotalDia: this.editForm.get(['valorTotalDia'])!.value,
      valorRegistradoDia: this.editForm.get(['valorRegistradoDia'])!.value,
      diferencia: this.editForm.get(['diferencia'])!.value,
      estado: this.editForm.get(['estado'])!.value,
    };
  }
}
