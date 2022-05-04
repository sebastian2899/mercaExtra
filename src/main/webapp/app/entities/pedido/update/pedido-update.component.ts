import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPedido, Pedido } from '../pedido.model';
import { PedidoService } from '../service/pedido.service';
import { IFacturaPedido } from '../factura-pedido';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { AlertService } from 'app/core/util/alert.service';

@Component({
  selector: 'jhi-pedido-update',
  templateUrl: './pedido-update.component.html',
})
export class PedidoUpdateComponent implements OnInit {
  @ViewChild('datosPedido', { static: true }) content: ElementRef | undefined;
  @ViewChild('messageUnviableDomiciliary', { static: true }) content2: ElementRef | undefined;
  @ViewChild('messageAlreadyOrderComming', { static: true }) content3: ElementRef | undefined;

  isSaving = false;
  facturasPedido?: IFacturaPedido[] | null;
  titulo?: string | null;
  mensajeFechaInvalida?: string | null;
  createOrder?: boolean | null;
  estadoPedido = ['Entregando', 'Finalizado'];
  pedidoEntregado?: boolean | null;

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

  constructor(
    protected pedidoService: PedidoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected modalService: NgbModal,
    protected alertService: AlertService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pedido }) => {
      if (pedido.id === undefined) {
        const today = dayjs().startOf('day');
        pedido.fechaPedido = today;
        this.titulo = 'Realizar Pedido';
        this.createOrder = true;
      } else {
        this.titulo = 'Actualizar Pedido';
        this.createOrder = false;
      }

      this.updateForm(pedido);
    });

    this.consultarFacturas();
  }

  previousState(): void {
    window.history.back();
  }

  consultarFacturas(): void {
    this.pedidoService.facturasPedido().subscribe({
      next: (res: HttpResponse<IFacturaPedido[]>) => {
        this.facturasPedido = res.body ?? [];
      },
      error: () => {
        this.facturasPedido = [];
      },
    });
  }

  ingresarDatos(idFactura: number): void {
    this.editForm.get(['idFactura'])?.setValue(idFactura);
    this.modalService.open(this.content, { size: 'lg' });
  }

  cancel(): void {
    this.modalService.dismissAll();
  }

  validateAviableDoimiciliary(): void {
    this.pedidoService.aviableDomiciliary().subscribe({
      next: (res: HttpResponse<number>) => {
        const resp = res.body;
        if (resp === 1) {
          this.modalService.open(this.content2, { backdrop: 'static', size: 'lg' });
        } else if (resp === 2) {
          this.modalService.open(this.content3, { backdrop: 'static', size: 'lg' });
        } else if (resp === 3) {
          this.save();
        }
      },
      error: () => {
        this.alertService.addAlert({
          type: 'danger',
          message: 'Error en la validación',
        });
      },
    });
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
    this.cancel();
    this.alertService.addAlert({
      type: 'success',
      message: 'Pedido registrado con exito, dirijase a la opcion (Mis pedidos) para ver el estado de su pedido.',
    });
    this.consultarFacturas();
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
