import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFactura, Factura } from '../factura.model';
import { FacturaService } from '../service/factura.service';
import { TipoFactura } from 'app/entities/enumerations/tipo-factura.model';
import { MetodoPago } from 'app/entities/enumerations/metodo-pago.model';

@Component({
  selector: 'jhi-factura-update',
  templateUrl: './factura-update.component.html',
})
export class FacturaUpdateComponent implements OnInit {
  isSaving = false;
  tipoFacturaValues = Object.keys(TipoFactura);
  metodoPagoValues = Object.keys(MetodoPago);

  editForm = this.fb.group({
    id: [],
    fechaCreacion: [],
    infoCiente: [],
    numeroFactura: [],
    tipoFactura: [],
    valorFactura: [],
    valorPagado: [],
    valorDeuda: [],
    estadoFactura: [],
    metodoPago: [],
    userName: [],
  });

  constructor(protected facturaService: FacturaService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factura }) => {
      if (factura.id === undefined) {
        const today = dayjs().startOf('day');
        factura.fechaCreacion = today;
      }

      this.updateForm(factura);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const factura = this.createFromForm();
    if (factura.id !== undefined) {
      this.subscribeToSaveResponse(this.facturaService.update(factura));
    } else {
      this.subscribeToSaveResponse(this.facturaService.create(factura));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactura>>): void {
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

  protected updateForm(factura: IFactura): void {
    this.editForm.patchValue({
      id: factura.id,
      fechaCreacion: factura.fechaCreacion ? factura.fechaCreacion.format(DATE_TIME_FORMAT) : null,
      infoCiente: factura.infoCiente,
      numeroFactura: factura.numeroFactura,
      tipoFactura: factura.tipoFactura,
      valorFactura: factura.valorFactura,
      valorPagado: factura.valorPagado,
      valorDeuda: factura.valorDeuda,
      estadoFactura: factura.estadoFactura,
      metodoPago: factura.metodoPago,
      userName: factura.userName,
    });
  }

  protected createFromForm(): IFactura {
    return {
      ...new Factura(),
      id: this.editForm.get(['id'])!.value,
      fechaCreacion: this.editForm.get(['fechaCreacion'])!.value
        ? dayjs(this.editForm.get(['fechaCreacion'])!.value, DATE_TIME_FORMAT)
        : undefined,
      infoCiente: this.editForm.get(['infoCiente'])!.value,
      numeroFactura: this.editForm.get(['numeroFactura'])!.value,
      tipoFactura: this.editForm.get(['tipoFactura'])!.value,
      valorFactura: this.editForm.get(['valorFactura'])!.value,
      valorPagado: this.editForm.get(['valorPagado'])!.value,
      valorDeuda: this.editForm.get(['valorDeuda'])!.value,
      estadoFactura: this.editForm.get(['estadoFactura'])!.value,
      metodoPago: this.editForm.get(['metodoPago'])!.value,
      userName: this.editForm.get(['userName'])!.value,
    };
  }
}
