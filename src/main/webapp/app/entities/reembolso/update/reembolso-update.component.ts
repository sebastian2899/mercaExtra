import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IReembolso, Reembolso } from '../reembolso.model';
import { ReembolsoService } from '../service/reembolso.service';
import { PedidoService } from 'app/entities/pedido/service/pedido.service';
import { IDatosPedidoReembolso } from '../DatosPedidoReembolso';

@Component({
  selector: 'jhi-reembolso-update',
  templateUrl: './reembolso-update.component.html',
})
export class ReembolsoUpdateComponent implements OnInit {
  isSaving = false;
  pedidosExpirados?: IDatosPedidoReembolso[] = [];
  addDescription = false;

  editForm = this.fb.group({
    id: [],
    idPedido: [],
    idDomiciliario: [],
    idFactura: [],
    descripcion: [],
    estado: [],
  });

  constructor(
    protected reembolsoService: ReembolsoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected pedioService: PedidoService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reembolso }) => {
      this.updateForm(reembolso);
    });
    this.consultarPedidosExpirados();
  }

  previousState(): void {
    window.history.back();
  }

  consultarPedidosExpirados(): void {
    this.pedioService.reembolsosPedidos().subscribe({
      next: (res: HttpResponse<IDatosPedidoReembolso[]>) => {
        this.pedidosExpirados = res.body ?? [];
      },
      error: () => {
        this.pedidosExpirados = [];
      },
    });
  }

  validateRefund(): void {
    this.addDescription = true;
  }

  aisgnarDatosReembolso(pedido: IDatosPedidoReembolso): void {
    this.editForm.get(['idPedido'])?.setValue(pedido.idPedido);
    this.editForm.get(['idDomiciliario'])?.setValue(pedido.idDomiciliario);
    this.editForm.get(['idFactura'])?.setValue(pedido.idFactura);
  }

  save(): void {
    this.isSaving = true;
    const reembolso = this.createFromForm();
    if (reembolso.id !== undefined) {
      this.subscribeToSaveResponse(this.reembolsoService.update(reembolso));
    } else {
      this.subscribeToSaveResponse(this.reembolsoService.create(reembolso));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReembolso>>): void {
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

  protected updateForm(reembolso: IReembolso): void {
    this.editForm.patchValue({
      id: reembolso.id,
      idPedido: reembolso.idPedido,
      idDomiciliario: reembolso.idDomiciliario,
      idFactura: reembolso.idFactura,
      descripcion: reembolso.descripcion,
      estado: reembolso.estado,
    });
  }

  protected createFromForm(): IReembolso {
    return {
      ...new Reembolso(),
      id: this.editForm.get(['id'])!.value,
      idPedido: this.editForm.get(['idPedido'])!.value,
      idDomiciliario: this.editForm.get(['idDomiciliario'])!.value,
      idFactura: this.editForm.get(['idFactura'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      estado: this.editForm.get(['estado'])!.value,
    };
  }
}
