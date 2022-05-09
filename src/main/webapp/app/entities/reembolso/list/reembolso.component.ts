import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IReembolso } from '../reembolso.model';
import { ReembolsoService } from '../service/reembolso.service';
import { ReembolsoDeleteDialogComponent } from '../delete/reembolso-delete-dialog.component';

@Component({
  selector: 'jhi-reembolso',
  templateUrl: './reembolso.component.html',
})
export class ReembolsoComponent implements OnInit {
  reembolsos?: IReembolso[];
  isLoading = false;

  constructor(protected reembolsoService: ReembolsoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.reembolsoService.query().subscribe({
      next: (res: HttpResponse<IReembolso[]>) => {
        this.isLoading = false;
        this.reembolsos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IReembolso): number {
    return item.id!;
  }

  delete(reembolso: IReembolso): void {
    const modalRef = this.modalService.open(ReembolsoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.reembolso = reembolso;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
