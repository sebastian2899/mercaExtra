import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { Domiciliario, IDomiciliario } from '../domiciliario.model';
import { DomiciliarioService } from '../service/domiciliario.service';
import { DomiciliarioDeleteDialogComponent } from '../delete/domiciliario-delete-dialog.component';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { EstadoDomiciliario } from 'app/entities/enumerations/estado-domiciliario.model';
import { TipoSalario } from 'app/entities/enumerations/tipo-salario.model';

@Component({
  selector: 'jhi-domiciliario',
  templateUrl: './domiciliario.component.html',
})
export class DomiciliarioComponent implements OnInit {
  domiciliary!: IDomiciliario | null;
  nombre!: string | null;
  horario!: string | null;
  domiciliarios?: IDomiciliario[];
  tipoSalarios = Object.keys(TipoSalario);
  tipoSalario!: TipoSalario;
  stateDomiciary = Object.keys(EstadoDomiciliario);
  estadoDomiciliario!: EstadoDomiciliario;
  isLoading = false;
  userLogin?: string | null;
  account?: Account | null;
  verInfo!: boolean;
  opcion = 'Ver Información';

  constructor(
    protected domiciliarioService: DomiciliarioService,
    protected modalService: NgbModal,
    protected accountService: AccountService
  ) {}

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
    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      if (this.account) {
        this.userLogin = this.account.login;
        this.userLogin === 'admin' ? (this.verInfo = false) : (this.verInfo = true);
      }
    });
  }

  domciliaryFilters(): void {
    this.domiciliary = new Domiciliario();
    this.isLoading = true;

    this.domiciliary.nombre = this.nombre;
    this.domiciliary.salario = this.tipoSalario;
    this.domiciliary.horario = this.horario;
    this.domiciliary.estado = this.estadoDomiciliario;

    this.domiciliarioService.domiciliariosFiltro(this.domiciliary).subscribe({
      next: (res: HttpResponse<IDomiciliario[]>) => {
        this.isLoading = false;
        this.domiciliarios = res.body ?? [];
      },
      error: () => {
        this.domiciliarios = [];
        this.isLoading = false;
      },
    });
  }

  changeLookInfo(): void {
    this.verInfo ? (this.verInfo = false) : (this.verInfo = true);
    this.verInfo ? (this.opcion = 'Ocultar') : (this.opcion = 'Ver Informacion');
  }

  stateDomiciliary(estado: EstadoDomiciliario): string {
    if (estado === 'EN_ENTREGA') {
      return 'bg-warning';
    }

    return 'bg-success';
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
