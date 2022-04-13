import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProveedor } from '../proveedor.model';
import { ProveedorService } from '../service/proveedor.service';

@Component({
  templateUrl: './proveedor-delete-dialog.component.html',
})
export class ProveedorDeleteDialogComponent {
  proveedor?: IProveedor;

  constructor(protected proveedorService: ProveedorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.proveedorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
