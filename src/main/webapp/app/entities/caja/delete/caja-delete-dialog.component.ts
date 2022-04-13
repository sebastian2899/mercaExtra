import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICaja } from '../caja.model';
import { CajaService } from '../service/caja.service';

@Component({
  templateUrl: './caja-delete-dialog.component.html',
})
export class CajaDeleteDialogComponent {
  caja?: ICaja;

  constructor(protected cajaService: CajaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cajaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
