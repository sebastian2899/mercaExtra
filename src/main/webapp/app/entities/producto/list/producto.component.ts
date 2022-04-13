import { Component, OnInit } from '@angular/core';
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

@Component({
  selector: 'jhi-producto',
  templateUrl: './producto.component.html',
})
export class ProductoComponent implements OnInit {
  productos?: IProducto[];
  producto?: IProducto;
  isLoading = false;
  account?: Account | null;
  pA = 1;
  categorias?: ICategoriaProducto[] | null;
  categoria?: string | null;
  nombre?: string | null;
  descripcion?: string | null;
  verAgotados = false;
  mostrarAgotados = false;
  productosAgotados?: IProducto[] | null;
  productosEscasos?: IProducto[] | null;

  constructor(
    protected productoService: ProductoService,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal,
    protected accountService: AccountService,
    protected categoriaService: CategoriaProductoService
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
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });
    this.consultarCategorias();
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
    this.consultarProductosAE();
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

  consultarProductosAE(): void {
    this.productoService.productosAgotados().subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.productosAgotados = res.body ?? [];
        if (this.productosAgotados.length >= 0) {
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
        if (this.productosEscasos.length >= 0) {
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
