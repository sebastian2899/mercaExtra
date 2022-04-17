import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IItemFacturaVenta } from '../item-factura-venta.model';
import { ItemFacturaVentaService } from '../service/item-factura-venta.service';

@Component({
  templateUrl: './item-factura-venta-delete-dialog.component.html',
})
export class ItemFacturaVentaDeleteDialogComponent {
  itemFacturaVenta?: IItemFacturaVenta;

  constructor(protected itemFacturaVentaService: ItemFacturaVentaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.itemFacturaVentaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
