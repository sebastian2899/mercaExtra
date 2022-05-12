import { HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DataUtils } from 'app/core/util/data-util.service';
import { ICategoriaProducto } from 'app/entities/categoria-producto/categoria-producto.model';
import { CategoriaProductoService } from 'app/entities/categoria-producto/service/categoria-producto.service';
import { IProducto, Producto } from 'app/entities/producto/producto.model';
import { ProductoService } from 'app/entities/producto/service/producto.service';

@Component({
  selector: 'jhi-product-home',
  templateUrl: './product-home.component.html',
  styleUrls: ['./product-home.component.scss'],
})
export class ProductHomeComponent implements OnInit {
  productos?: IProducto[] | null;
  categorias?: ICategoriaProducto[] | null;
  categoria?: string | null;
  producto?: IProducto | null;
  pA = 1;
  nombre?: string | null;
  descripcion?: string | null;

  constructor(
    protected productoService: ProductoService,
    protected categoriaService: CategoriaProductoService,
    protected dataUtils: DataUtils,
    private route: Router
  ) {}

  ngOnInit(): void {
    this.loadAll();
    this.consultarCategorias();
  }

  loadAll(): void {
    this.productoService.queryLogout().subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.productos = res.body ?? [];
      },
      error: () => {
        this.productos = [];
      },
    });
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

  login(): void {
    this.route.navigate(['login']);
  }

  createAccount(): void {
    this.route.navigate(['account/register']);
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
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
}
