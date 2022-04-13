import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategoriaProducto } from '../categoria-producto.model';
import { CategoriaProductoService } from '../service/categoria-producto.service';

@Component({
  templateUrl: './categoria-producto-delete-dialog.component.html',
})
export class CategoriaProductoDeleteDialogComponent {
  categoriaProducto?: ICategoriaProducto;

  constructor(protected categoriaProductoService: CategoriaProductoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.categoriaProductoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
