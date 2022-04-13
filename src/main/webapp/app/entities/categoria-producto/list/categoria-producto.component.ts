import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategoriaProducto } from '../categoria-producto.model';
import { CategoriaProductoService } from '../service/categoria-producto.service';
import { CategoriaProductoDeleteDialogComponent } from '../delete/categoria-producto-delete-dialog.component';

@Component({
  selector: 'jhi-categoria-producto',
  templateUrl: './categoria-producto.component.html',
})
export class CategoriaProductoComponent implements OnInit {
  categoriaProductos?: ICategoriaProducto[];
  isLoading = false;

  constructor(protected categoriaProductoService: CategoriaProductoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.categoriaProductoService.query().subscribe({
      next: (res: HttpResponse<ICategoriaProducto[]>) => {
        this.isLoading = false;
        this.categoriaProductos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICategoriaProducto): number {
    return item.id!;
  }

  delete(categoriaProducto: ICategoriaProducto): void {
    const modalRef = this.modalService.open(CategoriaProductoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.categoriaProducto = categoriaProducto;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
