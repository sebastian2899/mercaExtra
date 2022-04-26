import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPedido } from '../pedido.model';
import { PedidoService } from '../service/pedido.service';
import { PedidoDeleteDialogComponent } from '../delete/pedido-delete-dialog.component';

@Component({
  selector: 'jhi-pedido',
  templateUrl: './pedido.component.html',
})
export class PedidoComponent implements OnInit {
  pedidos?: IPedido[];
  isLoading = false;
  pedido?: IPedido | null;

  constructor(protected pedidoService: PedidoService, protected modalService: NgbModal) {}

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
  }

  pedidoComming(): void {
    this.pedidoService.pedidoInComming().subscribe({
      next: (res: HttpResponse<IPedido>) => {
        this.pedido = res.body;
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
