import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReembolso } from '../reembolso.model';
import { ReembolsoService } from '../service/reembolso.service';

@Component({
  templateUrl: './reembolso-delete-dialog.component.html',
})
export class ReembolsoDeleteDialogComponent {
  reembolso?: IReembolso;

  constructor(protected reembolsoService: ReembolsoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reembolsoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
