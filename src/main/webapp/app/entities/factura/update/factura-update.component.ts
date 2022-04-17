import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, FormControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFactura, Factura } from '../factura.model';
import { FacturaService } from '../service/factura.service';
import { TipoFactura } from 'app/entities/enumerations/tipo-factura.model';
import { MetodoPago } from 'app/entities/enumerations/metodo-pago.model';
import { IProducto } from 'app/entities/producto/producto.model';
import { DataUtils } from 'app/core/util/data-util.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IItemFacturaVenta, ItemFacturaVenta } from 'app/entities/item-factura-venta/item-factura-venta.model';
import { AlertService } from 'app/core/util/alert.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';

@Component({
  selector: 'jhi-factura-update',
  templateUrl: './factura-update.component.html',
})
export class FacturaUpdateComponent implements OnInit {
  @ViewChild('verCarroCompra', { static: true }) content: ElementRef | undefined;
  @ViewChild('llenarCarro', { static: true }) content2: ElementRef | undefined;

  isSaving = false;
  tipoFacturaValues = Object.keys(TipoFactura);
  metodoPagoValues = Object.keys(MetodoPago);
  productos?: IProducto[] | null;
  pA = 1;
  producto?: IProducto | null;
  cantidad?: number | null;
  productoSeleccionado?: IProducto | null;
  productosSeleccionados: IItemFacturaVenta[] = [];
  productoItem?: IItemFacturaVenta | null;
  productoNom?: string | null;
  tipoCategoria?: string | null;
  productoStorage?: IProducto | null;

  editForm = this.fb.group({
    id: [],
    fechaCreacion: [],
    infoCiente: [],
    numeroFactura: [],
    tipoFactura: [],
    valorFactura: [],
    producto: new FormControl(),
    cantidad: new FormControl(),
    valorPagado: [],
    valorDeuda: [],
    estadoFactura: [],
    metodoPago: [],
    userName: [],
  });

  constructor(
    protected facturaService: FacturaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected dataUtils: DataUtils,
    protected ngbModal: NgbModal,
    protected alertService: AlertService,
    protected storageService: StateStorageService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factura }) => {
      if (factura.id === undefined) {
        const today = dayjs().startOf('day');
        factura.fechaCreacion = today;
      }

      this.updateForm(factura);
      this.consultarProductosDisponibles();
    });
    this.productoStorage = this.storageService.getParametroProducto();
    if (this.productoStorage) {
      this.productoItem = new ItemFacturaVenta();
      this.productoItem.nombreProducto = this.productoStorage.nombre;
      this.productoItem.precioOriginal = this.productoStorage.precio;
      this.productosSeleccionados.push(this.productoItem);
    }
  }

  previousState(): void {
    window.history.back();
  }

  consultarProductosDisponibles(): void {
    this.facturaService.productosDisponibles().subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.productos = res.body ?? [];
      },
      error: () => {
        this.productos = [];
      },
    });
  }

  tipoCategoriaMethod(): void {
    if (this.tipoCategoria) {
      this.facturaService.productosCategoria(this.tipoCategoria).subscribe({
        next: (res: HttpResponse<IProducto[]>) => {
          this.productos = res.body ?? [];
        },
        error: () => {
          this.productos = [];
        },
      });
    }
  }

  agregarProducto(): void {
    let cantidad;
    if (this.productoSeleccionado) {
      if (this.cantidad) {
        cantidad = this.cantidad;
      }

      this.productoSeleccionado.cantidadSeleccionada = cantidad;

      this.productoItem = new ItemFacturaVenta();
      this.productoItem.nombreProducto = this.productoSeleccionado.nombre;
      this.productoItem.cantidad = this.productoSeleccionado.cantidadSeleccionada;
      this.productoItem.precioOriginal = this.productoSeleccionado.precio;
      this.productoItem.precio = this.productoSeleccionado.precio = this.productoSeleccionado.precio! * Number(cantidad);

      this.productosSeleccionados.push(this.productoItem);
    }

    this.ngbModal.dismissAll();
    this.alertService.addAlert({
      type: 'success',
      message: 'Producto Agregado al carrito de compras con exito.',
    });
  }

  verCarroCompras(): void {
    if (this.productosSeleccionados.length === 0) {
      this.alertService.addAlert({
        type: 'warning',
        message: 'Tu carro de compras esta vacío, por favor realiza por lo menos una compra.',
      });
    } else {
      this.ngbModal.open(this.content);
    }
  }

  eliminarProducto(producto: IItemFacturaVenta): void {
    const index = this.productosSeleccionados.indexOf(producto);
    if (index >= 0) {
      this.productosSeleccionados.splice(index, 1);
    }
    if (this.productosSeleccionados.length === 0) {
      this.ngbModal.dismissAll();
    }
  }

  llenarCarroCompra(producto: IProducto): void {
    this.productoNom = producto.nombre;

    this.productoSeleccionado = producto;

    this.ngbModal.open(this.content2);
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

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
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
