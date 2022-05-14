import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { IProducto } from 'app/entities/producto/producto.model';
import { ProductoService } from 'app/entities/producto/service/producto.service';
import { HttpResponse } from '@angular/common/http';
import { CajaService } from 'app/entities/caja/service/caja.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit, OnDestroy {
  @ViewChild('remember', { static: true }) content: ElementRef | undefined;
  account: Account | null = null;
  productos?: IProducto[] | null;
  intervalId?: any;
  respNumber?: number | null;

  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private router: Router,
    private productoService: ProductoService,
    protected cajaService: CajaService,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => (this.account = account));
    this.consultarProductos();
    this.rememberCreationCaja();
  }

  rememberCreationCaja(): void {
    this.intervalId = setInterval(() => {
      if (this.account?.login === 'admin') {
        this.cajaService.rememberCreationCaja().subscribe({
          next: (res: HttpResponse<number>) => {
            this.respNumber = res.body;
            this.respNumber === 1 ? clearInterval(this.intervalId) : this.modalService.open(this.content);
          },
        });
      }
    }, 3600000);
  }

  consultarProductos(): void {
    this.productoService.query().subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.productos = res.body ?? [];
      },
      error: () => {
        this.productos = [];
      },
    });
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
