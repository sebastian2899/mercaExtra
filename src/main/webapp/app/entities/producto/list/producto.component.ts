import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProducto, Producto } from '../producto.model';
import { ProductoService } from '../service/producto.service';
import { ProductoDeleteDialogComponent } from '../delete/producto-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { ICategoriaProducto } from 'app/entities/categoria-producto/categoria-producto.model';
import { CategoriaProductoService } from 'app/entities/categoria-producto/service/categoria-producto.service';
import { AlertService } from 'app/core/util/alert.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { IItemFacturaVenta } from 'app/entities/item-factura-venta/item-factura-venta.model';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-producto',
  templateUrl: './producto.component.html',
})
export class ProductoComponent implements OnInit {
  @ViewChild('mensajeAyuda', { static: true }) content: ElementRef | undefined;
  @ViewChild('carritoCompras', { static: true }) content2: ElementRef | undefined;

  productos?: IProducto[];
  producto?: IProducto;
  isLoading = false;
  account?: Account | null;
  pA = 1;
  categorias?: ICategoriaProducto[] | null;
  categoria?: string | null;
  nombre?: string | null;
  descripcion?: string | null;
  verAgotados?: boolean | null;
  mostrarAgotados = false;
  productosAgotados?: IProducto[] | null;
  productosEscasos?: IProducto[] | null;
  isAdmin?: boolean | null;
  aumento?: boolean | null;
  decremento?: boolean | null;
  opcion?: string | null;
  cantidadAplicar?: number | number;
  deshabilitarCambio?: boolean | null;
  existShoppingCar?: boolean | null;
  productosCarrito?: IItemFacturaVenta[] | null = [];

  constructor(
    protected productoService: ProductoService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    protected accountService: AccountService,
    protected categoriaService: CategoriaProductoService,
    protected alertService: AlertService,
    protected storageService: StateStorageService,
    protected router: Router
  ) {}

  loadAll(): void {
    this.isLoading = true;
    this.productoService.query().subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.isLoading = false;
        this.productos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.consultarCategorias();
    this.consultarProductosAE();
    const shopingCard = this.storageService.getCarrito();
    if (shopingCard) {
      this.existShoppingCar = true;
      this.productosCarrito = shopingCard;
    } else {
      this.existShoppingCar = false;
      this.productosCarrito = null;
    }

    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      if (this.account?.login === 'admin') {
        this.isAdmin = true;
      } else {
        this.isAdmin = false;
      }
    });
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  verMensajeAyuda(): void {
    this.modalService.open(this.content);
  }

  // Si existen productos en el localStorage se podran mostrar los prodcuctos que hay guardados en el momento pero para realizar
  // la compra se debera ir a la seccion de factura para completar el pedido y hacer las respectivas confirmaciones.
  verCarritoDeCompras(): void {
    this.modalService.open(this.content2, { backdrop: 'static', scrollable: true });
  }

  consultarCategorias(): void {
    this.categoriaService.query().subscribe({
      next: (res: HttpResponse<ICategoriaProducto[]>) => {
        this.categorias = res.body ?? [];
      },
      error: () => {
        this.categorias = [];
      },
    });
  }

  redirectionToShopingCar(): void {
    this.modalService.dismissAll();
    this.router.navigate(['factura/new']);
  }

  productosFiltro(): void {
    this.producto = new Producto();
    this.producto.nombre = this.nombre;
    this.producto.descripcion = this.descripcion;

    this.productoService.productosFiltro(this.producto).subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.productos = res.body ?? [];
      },
      error: () => {
        this.productos = [];
      },
    });
  }

  cancelarPorcentaje(): void {
    this.aumento = false;
    this.decremento = false;
    this.cantidadAplicar = 0;
    this.deshabilitarCambio = false;
  }

  productosPorCategoriaYFiltro(opcion: number): void {
    this.productoService.productosCategoriaFiltro(opcion, this.categoria!).subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.productos = res.body ?? [];
      },
      error: () => {
        this.productos = [];
      },
    });
  }

  aplicarAumento(): void {
    this.aumento = true;
    this.decremento = false;
    this.opcion = 'aumentar';
  }

  aplicarDecremento(): void {
    this.aumento = false;
    this.decremento = true;
    this.opcion = 'decrementar';
  }

  ejecutarCambioPrecio(opcion: string): void {
    if (this.cantidadAplicar) {
      if (this.cantidadAplicar < 1 || this.cantidadAplicar > 10) {
        this.deshabilitarCambio = true;
      } else {
        this.deshabilitarCambio = false;
        this.messageSuccess(opcion);
      }
    } else {
      this.deshabilitarCambio = true;
    }
  }

  messageSuccess(opcion: string): void {
    this.productoService.aplicarPorcentajeProducto(opcion, this.cantidadAplicar!).subscribe(() => {
      this.alertService.addAlert({
        type: 'success',
        message: 'Porcentaje aplicado con exito!',
      });
      this.loadAll();
    });
    this.cancelarPorcentaje();
  }

  consultarProductosAE(): void {
    this.productoService.productosAgotados().subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.productosAgotados = res.body ?? [];
        if (this.productosAgotados.length > 0) {
          this.verAgotados = true;
        } else {
          this.verAgotados = false;
        }
      },
      error: () => {
        this.productosAgotados = [];
      },
    });

    this.productoService.productosEscasos().subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.productosEscasos = res.body ?? [];
        if (this.productosEscasos.length > 0) {
          this.verAgotados = true;
        } else {
          this.verAgotados = false;
        }
      },
      error: () => {
        this.productosEscasos = [];
      },
    });
  }

  productosPorCategoria(): void {
    if (this.categoria) {
      this.productoService.productosCategoria(this.categoria).subscribe({
        next: (res: HttpResponse<IProducto[]>) => {
          this.productos = res.body ?? [];
        },
        error: () => {
          this.productos = [];
        },
      });
    }
  }

  cancel(): void {
    this.modalService.dismissAll();
  }

  ocultarAgotadosMethod(): void {
    this.mostrarAgotados = false;
  }

  mostrarAgotadosMethod(): void {
    this.mostrarAgotados = true;
  }

  trackId(index: number, item: IProducto): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(producto: IProducto): void {
    const modalRef = this.modalService.open(ProductoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.producto = producto;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
