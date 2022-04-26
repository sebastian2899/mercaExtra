import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFactura } from '../factura.model';
import { FacturaService } from '../service/factura.service';
import { FacturaDeleteDialogComponent } from '../delete/factura-delete-dialog.component';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { AlertService } from 'app/core/util/alert.service';
import { finalize, Observable } from 'rxjs';
import { MetodoPago } from 'app/entities/enumerations/metodo-pago.model';

@Component({
  selector: 'jhi-factura',
  templateUrl: './factura.component.html',
})
export class FacturaComponent implements OnInit {
  @ViewChild('recomprar', { static: true }) content: ElementRef | undefined;

  facturas?: IFactura[];
  isLoading = false;
  account?: Account | null;
  accountAdmin?: boolean | null;
  metodoPago = ['Contra entrega', 'Transaccion Bancaria', 'Tarjeta de credito'];
  metodoPagoSelect?: string | null;
  factura?: IFactura | null;
  valorFactura?: number | null;
  valorPagado?: number | null;
  deuda?: number | null;
  validateSaveRebuy = false;

  constructor(
    protected facturaService: FacturaService,
    protected modalService: NgbModal,
    protected accountService: AccountService,
    protected alertService: AlertService
  ) {}

  loadAll(): void {
    this.isLoading = true;

    this.facturaService.query().subscribe({
      next: (res: HttpResponse<IFactura[]>) => {
        this.isLoading = false;
        this.facturas = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });
    if (this.account?.login === 'admin') {
      this.loadAll();
      this.accountAdmin = true;
    } else {
      this.facturasUsuarios();
      this.accountAdmin = false;
    }
  }

  isAutenticated(): void {
    this.accountService.isAuthenticated();
  }

  openModal(idFactura: number): void {
    this.modalService.open(this.content, { size: 'lg', backdrop: 'static' });
    this.consultarValorFactura(idFactura);
  }

  confirmRepurcharse(): void {
    if (this.valorPagado && this.metodoPagoSelect === 'Contra entrega') {
      this.factura!.valorPagado = this.valorPagado;
      this.factura!.valorDeuda = this.deuda;
      this.factura!.metodoPago = MetodoPago.CONTRA_ENTREGA;
    } else if (this.metodoPagoSelect === 'Transaccion Bancaria') {
      this.factura!.metodoPago = MetodoPago.TRANSACCION_BANCARIA;
    }
    this.subscribeToSaveResponse(this.facturaService.rePurchaseInvoice(this.factura!));
  }

  consultarValorFactura(idFactura: number): number {
    this.facturaService.find(idFactura).subscribe({
      next: (res: HttpResponse<IFactura>) => {
        this.factura = res.body;
        this.valorFactura = this.factura?.valorFactura;
      },
    });

    return Number(this.valorFactura);
  }

  facturasUsuarios(): void {
    this.facturaService.facturasUsuario().subscribe({
      next: (res: HttpResponse<IFactura[]>) => {
        this.facturas = res.body ?? [];
      },
      error: () => {
        this.facturas = [];
      },
    });
  }

  calcularDeuda(): void {
    if (this.valorFactura && this.valorPagado) {
      const valorFactura = this.valorFactura;
      const valorPagado = this.valorPagado;
      this.deuda = Number(valorFactura) - Number(valorPagado);
      this.deuda > 0 ? (this.validateSaveRebuy = false) : (this.validateSaveRebuy = true);
    } else {
      this.validateSaveRebuy = false;
    }
  }

  closeModal(): void {
    this.modalService.dismissAll();
  }

  trackId(index: number, item: IFactura): number {
    return item.id!;
  }

  delete(factura: IFactura): void {
    const modalRef = this.modalService.open(FacturaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.factura = factura;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactura>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    if (this.accountAdmin) {
      this.loadAll();
    } else {
      this.facturasUsuarios();
    }

    this.closeModal();
  }

  protected onSaveError(): void {
    // Api for inheritance.
    this.closeModal();
  }

  protected onSaveFinalize(): void {
    this.validateSaveRebuy = false;
  }
}
