import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICaja } from '../caja.model';
import { CajaService } from '../service/caja.service';
import { CajaDeleteDialogComponent } from '../delete/caja-delete-dialog.component';

@Component({
  selector: 'jhi-caja',
  templateUrl: './caja.component.html',
})
export class CajaComponent implements OnInit {
  @ViewChild('remember', { static: true }) content: ElementRef | undefined;
  cajas?: ICaja[];
  isLoading = false;
  respNumber?: number | null;
  intervalId?: any;

  constructor(protected cajaService: CajaService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.cajaService.query().subscribe({
      next: (res: HttpResponse<ICaja[]>) => {
        this.isLoading = false;
        this.cajas = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.rememberCreationCaja();
  }

  rememberCreationCaja(): void {
    this.intervalId = setInterval(() => {
      this.cajaService.rememberCreationCaja().subscribe({
        next: (res: HttpResponse<number>) => {
          this.respNumber = res.body;
          this.respNumber === 1 ? clearInterval(this.intervalId) : this.modalService.open(this.content);
        },
      });
    }, 3600000);

    // this.cajaService.rememberCreationCaja().subscribe({
    //   next:
    //   (res:HttpResponse<string>) =>{
    //     this.mensaje = res.body;
    //     this.mensaje === " " ? null : this.modalService.open(this.content);
    //   }
    // });
  }

  trackId(index: number, item: ICaja): number {
    return item.id!;
  }

  delete(caja: ICaja): void {
    const modalRef = this.modalService.open(CajaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.caja = caja;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
