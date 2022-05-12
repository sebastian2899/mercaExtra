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
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-caja-update',
  templateUrl: './caja-update.component.html',
})
export class CajaUpdateComponent implements OnInit {
  isSaving = false;
  titulo?: string | null;

  editForm = this.fb.group({
    id: [],
    fechaCreacion: [],
    valorTotalDia: [],
    valorRegistradoDia: [],
    diferencia: [],
    estado: [],
  });

  constructor(
    protected cajaService: CajaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected alertService: AlertService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ caja }) => {
      if (caja.id === undefined) {
        const today = dayjs().startOf('day');
        caja.fechaCreacion = today;
        this.titulo = 'Crear Caja Diaria';
      } else {
        this.titulo = 'Actualizar Caja';
      }

      this.updateForm(caja);
    });

    this.consultarValorVendidoDia();
  }

  previousState(): void {
    window.history.back();
  }

  consultarValorVendidoDia(): void {
    this.cajaService.valorVendidoDia().subscribe({
      next: (res: HttpResponse<number>) => {
        const valor = res.body;
        if (valor) {
          const valueformat = valor.toFixed(0);
          valor > 0 ? this.editForm.get(['valorTotalDia'])?.setValue(valueformat) : this.editForm.get(['valorTotalDia'])?.setValue(0);
        }
      },
      error: () => {
        this.alertService.addAlert({
          type: 'danger',
          message: 'Error al consultar el valor vendido del dia.',
        });
      },
    });
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
