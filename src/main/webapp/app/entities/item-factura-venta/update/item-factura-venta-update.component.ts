import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IItemFacturaVenta, ItemFacturaVenta } from '../item-factura-venta.model';
import { ItemFacturaVentaService } from '../service/item-factura-venta.service';

@Component({
  selector: 'jhi-item-factura-venta-update',
  templateUrl: './item-factura-venta-update.component.html',
})
export class ItemFacturaVentaUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    idFactura: [],
    idProducto: [],
    cantidad: [],
    precio: [],
  });

  constructor(
    protected itemFacturaVentaService: ItemFacturaVentaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemFacturaVenta }) => {
      this.updateForm(itemFacturaVenta);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const itemFacturaVenta = this.createFromForm();
    if (itemFacturaVenta.id !== undefined) {
      this.subscribeToSaveResponse(this.itemFacturaVentaService.update(itemFacturaVenta));
    } else {
      this.subscribeToSaveResponse(this.itemFacturaVentaService.create(itemFacturaVenta));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItemFacturaVenta>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(itemFacturaVenta: IItemFacturaVenta): void {
    this.editForm.patchValue({
      id: itemFacturaVenta.id,
      idFactura: itemFacturaVenta.idFactura,
      idProducto: itemFacturaVenta.idProducto,
      cantidad: itemFacturaVenta.cantidad,
      precio: itemFacturaVenta.precio,
    });
  }

  protected createFromForm(): IItemFacturaVenta {
    return {
      ...new ItemFacturaVenta(),
      id: this.editForm.get(['id'])!.value,
      idFactura: this.editForm.get(['idFactura'])!.value,
      idProducto: this.editForm.get(['idProducto'])!.value,
      cantidad: this.editForm.get(['cantidad'])!.value,
      precio: this.editForm.get(['precio'])!.value,
    };
  }
}
