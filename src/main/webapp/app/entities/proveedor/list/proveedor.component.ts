import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProveedor } from '../proveedor.model';
import { ProveedorService } from '../service/proveedor.service';
import { ProveedorDeleteDialogComponent } from '../delete/proveedor-delete-dialog.component';

@Component({
  selector: 'jhi-proveedor',
  templateUrl: './proveedor.component.html',
})
export class ProveedorComponent implements OnInit {
  proveedors?: IProveedor[];
  isLoading = false;

  constructor(protected proveedorService: ProveedorService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.proveedorService.query().subscribe({
      next: (res: HttpResponse<IProveedor[]>) => {
        this.isLoading = false;
        this.proveedors = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IProveedor): number {
    return item.id!;
  }

  delete(proveedor: IProveedor): void {
    const modalRef = this.modalService.open(ProveedorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.proveedor = proveedor;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
