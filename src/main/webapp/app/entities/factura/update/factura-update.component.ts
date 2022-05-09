import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, FormControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFactura, Factura } from '../factura.model';
import { FacturaService } from '../service/factura.service';
import { TipoFactura } from 'app/entities/enumerations/tipo-factura.model';
import { MetodoPago } from 'app/entities/enumerations/metodo-pago.model';
import { IProducto, Producto } from 'app/entities/producto/producto.model';
import { DataUtils } from 'app/core/util/data-util.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IItemFacturaVenta, ItemFacturaVenta } from 'app/entities/item-factura-venta/item-factura-venta.model';
import { AlertService } from 'app/core/util/alert.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { ProductoService } from 'app/entities/producto/service/producto.service';

@Component({
  selector: 'jhi-factura-update',
  templateUrl: './factura-update.component.html',
})
export class FacturaUpdateComponent implements OnInit {
  @ViewChild('verCarroCompra', { static: true }) content: ElementRef | undefined;
  @ViewChild('llenarCarro', { static: true }) content2: ElementRef | undefined;
  @ViewChild('validarCompra', { static: true }) content3: ElementRef | undefined;
  @ViewChild('verCarroCompra2', { static: true }) content4: ElementRef | undefined;
  @ViewChild('cantidadInvalida', { static: true }) content5: ElementRef | undefined;
  @ViewChild('carro torage', { static: true }) content6: ElementRef | undefined;

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
  productoNuevo = true;
  account?: Account | null;
  mensaje?: string | null;
  nombre?: string | null;
  productoFiltro?: IProducto | null;
  numeroConsignacion = '111-222-333-444';
  contadorCarrito = 0;
  totalFactura = 0;
  carroCompStorage?: IItemFacturaVenta[] | null = [];
  disableAdd?: boolean | null;

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
    protected storageService: StateStorageService,
    protected accountService: AccountService,
    protected productoService: ProductoService,
    protected route: Router
  ) {}

  ngOnInit(): void {
    const carrito = this.storageService.getCarrito();
    if (carrito) {
      this.productosSeleccionados = carrito;
      this.productosSeleccionados.length > 0 ? this.ngbModal.open(this.content, { backdrop: 'static', size: 'lg' }) : undefined;
    }

    /* si el carrode compras al refrescar la pantalla tiene uno o mas productos,
     se le setan los valores a el contador para identificar cuantos productos 
     tiene el carro de CompraService.
    */
    if (this.productosSeleccionados.length > 0) {
      this.productosSeleccionados.forEach(() => {
        this.contadorCarrito++;
      });
    }

    this.activatedRoute.data.subscribe(({ factura }) => {
      if (factura.id === undefined) {
        const today = dayjs().startOf('day');
        factura.fechaCreacion = today;
      } else {
        this.productosSeleccionados = factura.itemsPorFactura;
      }

      this.updateForm(factura);
      this.consultarProductosDisponibles();
    });
    this.productoStorage = this.storageService.getParametroProducto();
    if (this.productoStorage) {
      this.llenarCarroCompra(this.productoStorage);

      this.storageService.clearUrlProducto();
    }

    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });
    this.editForm.get(['infoCiente'])?.setValue(this.account?.login.toString());
  }

  previousState(): void {
    window.history.back();
  }

  confirmarCompra(): void {
    this.calcularValores();
    this.ngbModal.open(this.content3, { size: 'lg', backdrop: 'static', scrollable: true });
  }

  carroComprasStorage(): void {
    this.carroCompStorage = this.storageService.getCarrito();
    this.ngbModal.open(this.content6);
  }

  productosPorFiltro(): void {
    this.productoFiltro = new Producto();
    this.productoFiltro.nombre = this.nombre;

    this.productoService.productosFiltro(this.productoFiltro).subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.productos = res.body ?? [];
      },
      error: () => [(this.productos = [])],
    });
  }

  consultarProductosDisponibles(): void {
    this.facturaService.productosDisponibles().subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.productos = res.body ?? [];
        this.productos.forEach(element => {
          const dec = (Number(element.precioDescuento) * Number(element.precio)) / 100;
          const precioFormat = Number(element.precio) - dec;
          element.precioConDescuento = Number(precioFormat.toFixed(0));
        });
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
    if (this.cantidad) {
      cantidad = this.cantidad;
    }

    if (this.productosSeleccionados.length && this.productoSeleccionado) {
      for (let i = 0; i < this.productosSeleccionados.length; i++) {
        if (this.productosSeleccionados[i].idProducto === this.productoSeleccionado.id) {
          this.productosSeleccionados[i].cantidad! += Number(this.cantidad!);
          if (this.productosSeleccionados[i].cantidad! > this.productoSeleccionado.cantidad!) {
            this.mensaje = `No se puede seleccionar el producto, ya que has llegado al tope disponible total
                    de productos. Disponibles: ${String(this.productoSeleccionado.cantidad)}, Cantidad Seleccionada: ${String(
              this.productosSeleccionados[i].cantidad!
            )}`;
            this.ngbModal.open(this.content5);
            this.productoNuevo = false;
          } else {
            if (this.productoSeleccionado.precioDescuento) {
              const desc = (Number(this.productoSeleccionado.precio) * Number(this.productoSeleccionado.precioDescuento)) / 100;
              const valueDiscount = Number(this.productoSeleccionado.precio) - desc;
              this.productosSeleccionados[i].precio! += valueDiscount * this.cantidad!;
              this.storageService.storeCarrito(this.productosSeleccionados);
            } else {
              const totalTemp = this.cantidad! * this.productoSeleccionado.precio!;
              this.productosSeleccionados[i].precio! += totalTemp;
              this.storageService.storeCarrito(this.productosSeleccionados);
            }
            this.productoNuevo = false;
            this.ngbModal.dismissAll();
            this.mensajeProductoAddSuccess();
            break;
          }
        } else {
          this.productoNuevo = true;
        }
      }
    }

    if (this.productoSeleccionado && this.productoNuevo && cantidad) {
      if (cantidad > this.productoSeleccionado.cantidad!) {
        this.mensaje = `No se puede agregar el producto ${String(this.productoSeleccionado.nombre)}, ya que 
          se estan seleccionando ${String(cantidad)} y solo hay ${String(this.productoSeleccionado.cantidad)} disponibles.`;
        this.ngbModal.open(this.content5);
      } else {
        this.productoSeleccionado.cantidadSeleccionada = cantidad;
        this.productoItem = new ItemFacturaVenta();
        this.productoItem.idProducto = this.productoSeleccionado.id;
        this.productoItem.nombreProducto = this.productoSeleccionado.nombre;
        this.productoItem.cantidad = this.productoSeleccionado.cantidadSeleccionada;
        this.productoItem.precioOriginal = this.productoSeleccionado.precio;
        if (this.productoSeleccionado.precioDescuento) {
          const desc = (Number(this.productoSeleccionado.precio) * Number(this.productoSeleccionado.precioDescuento)) / 100;
          const valueDiscount = Number(this.productoSeleccionado.precio) - desc;
          this.productoItem.precioDesc = Number(valueDiscount.toFixed(0));
          const totalValue = Number(valueDiscount) * Number(cantidad);
          this.productoItem.precio = Number(totalValue.toFixed(0));
        } else {
          this.productoItem.precio = Number(this.productoSeleccionado.precio) * cantidad;
        }

        this.productosSeleccionados.push(this.productoItem);
        this.storageService.storeCarrito(this.productosSeleccionados);
        this.mensajeProductoAddSuccess();
        this.ngbModal.dismissAll();
        this.contadorCarrito++;
      }
    }
    this.cantidad = null;
    this.calcularValores();
  }

  mensajeProductoAddSuccess(): void {
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
      this.calcularValores();
      this.ngbModal.open(this.content, { size: 'lg', backdrop: 'static', scrollable: true });
    }
  }

  deleteAllElementsCarrito(): void {
    this.productosSeleccionados.splice(0, this.productosSeleccionados.length);
    this.storageService.storeCarrito(this.productosSeleccionados);
    this.calcularValores();
    this.totalFactura = 0;
    this.contadorCarrito = 0;
  }

  verCarroCompras2(): void {
    this.ngbModal.open(this.content4, { size: 'lg', scrollable: true });
  }

  calcularValores(): void {
    let valorFactura = 0;
    this.productosSeleccionados.forEach(element => {
      valorFactura += element.precio!;
    });

    const valFormar = Number(valorFactura.toFixed(0));
    this.editForm.get(['valorFactura'])?.setValue(valFormar);
    this.totalFactura = valFormar;
    this.editForm.get(['valorDeuda'])?.setValue(valFormar);
  }

  restarValores(): void {
    const valorFactura = this.editForm.get(['valorFactura'])!.value;
    const valorPagado = this.editForm.get(['valorPagado'])!.value;

    const valorDeuda = Number(valorFactura) - Number(valorPagado);

    this.editForm.get(['valorDeuda'])?.setValue(valorDeuda);
  }

  cancel(): void {
    this.ngbModal.dismissAll();
  }

  eliminarProducto(producto: IItemFacturaVenta): void {
    const index = this.productosSeleccionados.indexOf(producto);
    if (index >= 0) {
      this.productosSeleccionados.splice(index, 1);
      this.storageService.storeCarrito(this.productosSeleccionados);
      this.contadorCarrito--;
      this.calcularValores();
    }
    if (this.productosSeleccionados.length === 0) {
      this.ngbModal.dismissAll();
    }
  }

  llenarCarroCompra(producto: IProducto): void {
    this.productoNom = producto.nombre;

    this.productoSeleccionado = producto;

    this.ngbModal.open(this.content2, { backdrop: 'static' });
  }

  validationValueAmount(): void {
    if (this.cantidad! > 10) {
      this.disableAdd = true;
    } else {
      this.disableAdd = false;
    }
  }

  ChangeValuesShopingCard(opcion: string, item: IItemFacturaVenta): void {
    const index = this.productosSeleccionados.indexOf(item);
    if (opcion === 'add') {
      this.productosSeleccionados[index].cantidad! += 1;

      if (this.productosSeleccionados[index].precioDesc!) {
        this.productosSeleccionados[index].precio! += Number(this.productosSeleccionados[index].precioDesc!.toFixed(0));
      } else {
        this.productosSeleccionados[index].precio! += Number(this.productosSeleccionados[index].precioOriginal!.toFixed(0));
      }

      this.calcularValores();

      this.storageService.storeCarrito(this.productosSeleccionados);
    } else if (opcion === 'subs') {
      this.productosSeleccionados[index].cantidad! -= 1;

      if (this.productosSeleccionados[index].precioDesc) {
        this.productosSeleccionados[index].precio! -= Number(this.productosSeleccionados[index].precioDesc!.toFixed(0));
      } else {
        this.productosSeleccionados[index].precio! -= Number(this.productosSeleccionados[index].precioDesc?.toFixed(0));
      }
      this.calcularValores();
      this.storageService.storeCarrito(this.productosSeleccionados);
    }
  }

  save(): void {
    this.isSaving = true;
    const factura = this.createFromForm();
    if (factura.id !== undefined) {
      this.subscribeToSaveResponse(this.facturaService.update(factura));
    } else {
      factura.itemsPorFactura = this.productosSeleccionados;
      this.subscribeToSaveResponse(this.facturaService.create(factura));
      this.cancel();
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
    // cuando se complete la Compra, se limpia el stateStorage de el carrito de compras para que quede vacio
    this.storageService.clearCarrito();
    this.route.navigate(['/pedido/new']);
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
