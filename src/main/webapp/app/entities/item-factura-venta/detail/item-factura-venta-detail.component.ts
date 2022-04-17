import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IItemFacturaVenta } from '../item-factura-venta.model';

@Component({
  selector: 'jhi-item-factura-venta-detail',
  templateUrl: './item-factura-venta-detail.component.html',
})
export class ItemFacturaVentaDetailComponent implements OnInit {
  itemFacturaVenta: IItemFacturaVenta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemFacturaVenta }) => {
      this.itemFacturaVenta = itemFacturaVenta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
