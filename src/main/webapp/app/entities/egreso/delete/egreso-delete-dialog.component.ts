import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEgreso } from '../egreso.model';
import { EgresoService } from '../service/egreso.service';

@Component({
  templateUrl: './egreso-delete-dialog.component.html',
})
export class EgresoDeleteDialogComponent {
  egreso?: IEgreso;

  constructor(protected egresoService: EgresoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.egresoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
