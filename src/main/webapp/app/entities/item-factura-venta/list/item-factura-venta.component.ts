import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IItemFacturaVenta } from '../item-factura-venta.model';
import { ItemFacturaVentaService } from '../service/item-factura-venta.service';
import { ItemFacturaVentaDeleteDialogComponent } from '../delete/item-factura-venta-delete-dialog.component';

@Component({
  selector: 'jhi-item-factura-venta',
  templateUrl: './item-factura-venta.component.html',
})
export class ItemFacturaVentaComponent implements OnInit {
  itemFacturaVentas?: IItemFacturaVenta[];
  isLoading = false;

  constructor(protected itemFacturaVentaService: ItemFacturaVentaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.itemFacturaVentaService.query().subscribe({
      next: (res: HttpResponse<IItemFacturaVenta[]>) => {
        this.isLoading = false;
        this.itemFacturaVentas = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IItemFacturaVenta): number {
    return item.id!;
  }

  delete(itemFacturaVenta: IItemFacturaVenta): void {
    const modalRef = this.modalService.open(ItemFacturaVentaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.itemFacturaVenta = itemFacturaVenta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
