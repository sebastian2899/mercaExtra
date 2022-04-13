import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDomiciliario } from '../domiciliario.model';
import { DomiciliarioService } from '../service/domiciliario.service';
import { DomiciliarioDeleteDialogComponent } from '../delete/domiciliario-delete-dialog.component';

@Component({
  selector: 'jhi-domiciliario',
  templateUrl: './domiciliario.component.html',
})
export class DomiciliarioComponent implements OnInit {
  domiciliarios?: IDomiciliario[];
  isLoading = false;

  constructor(protected domiciliarioService: DomiciliarioService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.domiciliarioService.query().subscribe({
      next: (res: HttpResponse<IDomiciliario[]>) => {
        this.isLoading = false;
        this.domiciliarios = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDomiciliario): number {
    return item.id!;
  }

  delete(domiciliario: IDomiciliario): void {
    const modalRef = this.modalService.open(DomiciliarioDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.domiciliario = domiciliario;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
