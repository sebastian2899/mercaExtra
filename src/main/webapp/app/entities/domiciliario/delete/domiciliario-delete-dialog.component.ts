import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDomiciliario } from '../domiciliario.model';
import { DomiciliarioService } from '../service/domiciliario.service';

@Component({
  templateUrl: './domiciliario-delete-dialog.component.html',
})
export class DomiciliarioDeleteDialogComponent {
  domiciliario?: IDomiciliario;

  constructor(protected domiciliarioService: DomiciliarioService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.domiciliarioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
