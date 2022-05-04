import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPedido } from '../pedido.model';
import { PedidoService } from '../service/pedido.service';
import { PedidoDeleteDialogComponent } from '../delete/pedido-delete-dialog.component';
import { IFactura } from 'app/entities/factura/factura.model';
import { FacturaService } from 'app/entities/factura/service/factura.service';
import { NotificacionService } from 'app/entities/notificacion/service/notificacion.service';
import { INotificacion } from 'app/entities/notificacion/notificacion.model';
import { AlertService } from 'app/core/util/alert.service';
import dayjs from 'dayjs';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-pedido',
  templateUrl: './pedido.component.html',
})
export class PedidoComponent implements OnInit {
  @ViewChild('detalleFactura', { static: true }) content: ElementRef | undefined;
  @ViewChild('advertencia', { static: true }) content2: ElementRef | undefined;
  @ViewChild('pedidoEntregado', { static: true }) content3: ElementRef | undefined;

  pedidos?: IPedido[];
  isLoading = false;
  pedido?: IPedido | null;
  factura?: IFactura | null;
  notificacion?: string | null;
  fecha?: dayjs.Dayjs | null;
  account?: Account | null;
  intervalId?: any;

  constructor(
    protected pedidoService: PedidoService,
    protected modalService: NgbModal,
    protected facturaService: FacturaService,
    protected notificacionService: NotificacionService,
    protected alertService: AlertService,
    protected accountService: AccountService
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.pedidoService.query().subscribe({
      next: (res: HttpResponse<IPedido[]>) => {
        this.isLoading = false;
        this.pedidos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });

    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });
  }

  openModal(): void {
    this.modalService.open(this.content, { backdrop: 'static', size: 'lg', scrollable: true });
  }

  verDetalle(id: number): void {
    this.facturaService.find(id).subscribe({
      next: (res: HttpResponse<IFactura>) => {
        this.factura = res.body;
        if (this.factura) {
          this.openModal();
        }
      },
      error: () => {
        this.factura = null;
      },
    });
  }

  pedidosPorFecha(): void {
    if (this.fecha) {
      this.pedidoService.pedidosFecha(this.fecha.toString()).subscribe({
        next: (res: HttpResponse<IPedido[]>) => {
          this.pedidos = res.body ?? [];
        },
        error: () => {
          this.pedidos = [];
        },
      });
    }
  }

  validarEntrega(): void {
    this.modalService.open(this.content3, { backdrop: 'static', size: 'lg' });
  }

  consultarNotificacionPedido(id: number): void {
    this.notificacionService.find(id).subscribe({
      next: (res: HttpResponse<INotificacion>) => {
        this.notificacion = res.body?.descripcion;
      },
      error: () => {
        this.notificacion = null;
      },
    });
  }

  pedidoComming(): void {
    this.pedidoService.pedidoInComming().subscribe({
      next: (res: HttpResponse<IPedido>) => {
        this.pedido = res.body;
        if (this.pedido) {
          this.consultarNotificacionPedido(this.pedido.idNotificacion!);
          // this.cronPedido();
        }
      },
      error: () => {
        this.pedido = null;
      },
    });
  }

  ngOnInit(): void {
    this.pedidoComming();
    this.loadAll();
  }

  // cronPedido():void{
  //   let contador =0;
  //   if(this.pedido){
  //    this.intervalId = setInterval(() => {
  //       contador++;
  //       this.alertService.addAlert({
  //         type:'info',
  //         message:`${String(contador)}`
  //       })
  //       if(contador === 4){
  //         clearInterval( this.intervalId);
  //       }
  // }, 1000);

  //   }

  // }

  pedidoFinalizado(): void {
    if (this.pedido) {
      this.pedidoService.pedidoFinalizado(this.pedido).subscribe({
        next: () => {
          this.reload();
          this.modalService.dismissAll();
        },
        error: () => {
          this.alertService.addAlert({
            type: 'danger',
            message: 'Error al completar el pedido',
          });
          this.modalService.dismissAll();
        },
      });
    }
  }

  reload(): void {
    window.location.reload();
  }

  cancel(): void {
    this.modalService.dismissAll();
  }

  modalAdvertencia(): void {
    this.modalService.open(this.content2, { backdrop: 'static', size: 'lg' });
  }

  trackId(index: number, item: IPedido): number {
    return item.id!;
  }

  delete(pedido: IPedido): void {
    const modalRef = this.modalService.open(PedidoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pedido = pedido;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
