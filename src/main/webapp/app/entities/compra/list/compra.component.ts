import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { Compra, ICompra } from '../compra.model';
import { CompraService } from '../service/compra.service';
import { CompraDeleteDialogComponent } from '../delete/compra-delete-dialog.component';
import { TipoFactura } from 'app/entities/enumerations/tipo-factura.model';
import { IProveedor } from 'app/entities/proveedor/proveedor.model';
import { ProveedorService } from 'app/entities/proveedor/service/proveedor.service';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-compra',
  templateUrl: './compra.component.html',
})
export class CompraComponent implements OnInit {
  @ViewChild('itemsFacturaCompra', { static: true }) content: ElementRef | undefined;

  compras?: ICompra[];
  compra?: ICompra | null;
  isLoading = false;
  tipoFacturas = Object.keys(TipoFactura);
  tipoFactura?: TipoFactura | null;
  estados = ['Deuda', 'Pagada'];
  estado?: string | null;
  fecha?: dayjs.Dayjs | null;
  numeroFactura?: string | null;
  idProveedor?: number | null;
  proveedores?: IProveedor[] | null;

  constructor(protected compraService: CompraService, protected modalService: NgbModal, protected proveedorService: ProveedorService) {}

  loadAll(): void {
    this.isLoading = true;
    this.compraService.query().subscribe({
      next: (res: HttpResponse<ICompra[]>) => {
        this.isLoading = false;
        this.compras = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  findProveedores(): void {
    this.proveedorService.query().subscribe({
      next: (res: HttpResponse<IProveedor[]>) => {
        this.proveedores = res.body ?? [];
      },
    });
  }

  back(): void {
    this.modalService.dismissAll();
  }

  findItemsCompra(id: number): void {
    this.compraService.find(id).subscribe({
      next: (res: HttpResponse<ICompra>) => {
        this.compra = res.body;
        this.modalService.open(this.content, { backdrop: 'static', size: 'lg', scrollable: true });
      },
      error: () => (this.compra = null),
    });
  }

  compraFilters(): void {
    this.compra = new Compra();
    this.compra.tipoFactura = this.tipoFactura;
    this.compra.estado = this.estado;
    this.compra.idProveedor = this.idProveedor;
    this.compra.numeroFactura = this.numeroFactura;

    this.compraService.compraFilters(this.compra, this.fecha!.toString()).subscribe({
      next: (res: HttpResponse<ICompra[]>) => {
        this.compras = res.body ?? [];
      },
      error: () => {
        this.compras = [];
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.findProveedores();
  }

  trackId(index: number, item: ICompra): number {
    return item.id!;
  }

  delete(compra: ICompra): void {
    const modalRef = this.modalService.open(CompraDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.compra = compra;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
