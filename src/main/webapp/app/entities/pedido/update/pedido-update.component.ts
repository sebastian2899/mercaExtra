import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPedido, Pedido } from '../pedido.model';
import { PedidoService } from '../service/pedido.service';

@Component({
  selector: 'jhi-pedido-update',
  templateUrl: './pedido-update.component.html',
})
export class PedidoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fechaPedido: [],
    direccion: [],
    estado: [],
    infoDomicilio: [],
    idDomiciliario: [],
    idNotificacion: [],
    idFactura: [],
  });

  constructor(protected pedidoService: PedidoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pedido }) => {
      if (pedido.id === undefined) {
        const today = dayjs().startOf('day');
        pedido.fechaPedido = today;
      }

      this.updateForm(pedido);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pedido = this.createFromForm();
    if (pedido.id !== undefined) {
      this.subscribeToSaveResponse(this.pedidoService.update(pedido));
    } else {
      this.subscribeToSaveResponse(this.pedidoService.create(pedido));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPedido>>): void {
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

  protected updateForm(pedido: IPedido): void {
    this.editForm.patchValue({
      id: pedido.id,
      fechaPedido: pedido.fechaPedido ? pedido.fechaPedido.format(DATE_TIME_FORMAT) : null,
      direccion: pedido.direccion,
      estado: pedido.estado,
      infoDomicilio: pedido.infoDomicilio,
      idDomiciliario: pedido.idDomiciliario,
      idNotificacion: pedido.idNotificacion,
      idFactura: pedido.idFactura,
    });
  }

  protected createFromForm(): IPedido {
    return {
      ...new Pedido(),
      id: this.editForm.get(['id'])!.value,
      fechaPedido: this.editForm.get(['fechaPedido'])!.value
        ? dayjs(this.editForm.get(['fechaPedido'])!.value, DATE_TIME_FORMAT)
        : undefined,
      direccion: this.editForm.get(['direccion'])!.value,
      estado: this.editForm.get(['estado'])!.value,
      infoDomicilio: this.editForm.get(['infoDomicilio'])!.value,
      idDomiciliario: this.editForm.get(['idDomiciliario'])!.value,
      idNotificacion: this.editForm.get(['idNotificacion'])!.value,
      idFactura: this.editForm.get(['idFactura'])!.value,
    };
  }
}
