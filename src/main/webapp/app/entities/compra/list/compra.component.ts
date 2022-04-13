import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompra } from '../compra.model';
import { CompraService } from '../service/compra.service';
import { CompraDeleteDialogComponent } from '../delete/compra-delete-dialog.component';

@Component({
  selector: 'jhi-compra',
  templateUrl: './compra.component.html',
})
export class CompraComponent implements OnInit {
  compras?: ICompra[];
  isLoading = false;

  constructor(protected compraService: CompraService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.compraService.query().subscribe({
      next: (res: HttpResponse<ICompra[]>) => {
        this.isLoading = false;
        this.compras = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICompra): number {
    return item.id!;
  }

  delete(compra: ICompra): void {
    const modalRef = this.modalService.open(CompraDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.compra = compra;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
