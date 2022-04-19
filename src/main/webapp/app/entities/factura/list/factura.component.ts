import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFactura } from '../factura.model';
import { FacturaService } from '../service/factura.service';
import { FacturaDeleteDialogComponent } from '../delete/factura-delete-dialog.component';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

@Component({
  selector: 'jhi-factura',
  templateUrl: './factura.component.html',
})
export class FacturaComponent implements OnInit {
  facturas?: IFactura[];
  isLoading = false;
  account?: Account | null;
  accountAdmin?: boolean | null;

  constructor(protected facturaService: FacturaService, protected modalService: NgbModal, protected accountService: AccountService) {}

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
}
