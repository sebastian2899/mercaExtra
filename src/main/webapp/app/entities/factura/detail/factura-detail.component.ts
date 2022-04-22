import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';

import { IFactura } from '../factura.model';

@Component({
  selector: 'jhi-factura-detail',
  templateUrl: './factura-detail.component.html',
})
export class FacturaDetailComponent implements OnInit {
  factura: IFactura | null = null;
  account?: Account | null;

  constructor(protected activatedRoute: ActivatedRoute, protected accountService: AccountService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factura }) => {
      this.factura = factura;
    });
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
