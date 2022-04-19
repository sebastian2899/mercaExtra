import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { IProducto } from '../producto.model';
import { DataUtils } from 'app/core/util/data-util.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { StateStorageService } from 'app/core/auth/state-storage.service';

@Component({
  selector: 'jhi-producto-detail',
  templateUrl: './producto-detail.component.html',
})
export class ProductoDetailComponent implements OnInit {
  producto: IProducto | null = null;
  account?: Account | null;
  valorConDescuento?: number | null;

  constructor(
    protected dataUtils: DataUtils,
    protected activatedRoute: ActivatedRoute,
    protected accountService: AccountService,
    protected storageService: StateStorageService,
    protected router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ producto }) => {
      this.producto = producto;
    });
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });
    this.calcularDescuentoProducto();
  }

  calcularDescuentoProducto(): void {
    if (this.producto?.precioDescuento) {
      const descuento = (this.producto.precioDescuento * this.producto.precio!) / 100;
      this.valorConDescuento = this.producto.precio! - Number(descuento);
    }
  }

  pasoParametroProducto(producto: IProducto): void {
    this.storageService.pasoParametroProducto(producto);
    this.router.navigate(['factura/new']);
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
