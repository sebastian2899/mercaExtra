import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmpleado } from '../empleado.model';
import { EmpleadoService } from '../service/empleado.service';
import { EmpleadoDeleteDialogComponent } from '../delete/empleado-delete-dialog.component';

@Component({
  selector: 'jhi-empleado',
  templateUrl: './empleado.component.html',
})
export class EmpleadoComponent implements OnInit {
  empleados?: IEmpleado[];
  isLoading = false;

  constructor(protected empleadoService: EmpleadoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.empleadoService.query().subscribe({
      next: (res: HttpResponse<IEmpleado[]>) => {
        this.isLoading = false;
        this.empleados = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEmpleado): number {
    return item.id!;
  }

  delete(empleado: IEmpleado): void {
    const modalRef = this.modalService.open(EmpleadoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.empleado = empleado;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
