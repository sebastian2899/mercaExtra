import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { INotificacion } from '../notificacion.model';
import { NotificacionService } from '../service/notificacion.service';
import { NotificacionDeleteDialogComponent } from '../delete/notificacion-delete-dialog.component';

@Component({
  selector: 'jhi-notificacion',
  templateUrl: './notificacion.component.html',
})
export class NotificacionComponent implements OnInit {
  notificacions?: INotificacion[];
  isLoading = false;

  constructor(protected notificacionService: NotificacionService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.notificacionService.query().subscribe({
      next: (res: HttpResponse<INotificacion[]>) => {
        this.isLoading = false;
        this.notificacions = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: INotificacion): number {
    return item.id!;
  }

  delete(notificacion: INotificacion): void {
    const modalRef = this.modalService.open(NotificacionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.notificacion = notificacion;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
