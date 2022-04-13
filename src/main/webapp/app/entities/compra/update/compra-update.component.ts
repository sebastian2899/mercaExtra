import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICompra, Compra } from '../compra.model';
import { CompraService } from '../service/compra.service';
import { TipoFactura } from 'app/entities/enumerations/tipo-factura.model';

@Component({
  selector: 'jhi-compra-update',
  templateUrl: './compra-update.component.html',
})
export class CompraUpdateComponent implements OnInit {
  isSaving = false;
  tipoFacturaValues = Object.keys(TipoFactura);

  editForm = this.fb.group({
    id: [],
    fechaCreacion: [],
    numeroFactura: [],
    tipoFactura: [],
    informacionProovedor: [],
    idProveedor: [],
    valorFactura: [],
    valorPagado: [],
    valorDeuda: [],
    estado: [],
  });

  constructor(protected compraService: CompraService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compra }) => {
      this.updateForm(compra);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const compra = this.createFromForm();
    if (compra.id !== undefined) {
      this.subscribeToSaveResponse(this.compraService.update(compra));
    } else {
      this.subscribeToSaveResponse(this.compraService.create(compra));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompra>>): void {
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

  protected updateForm(compra: ICompra): void {
    this.editForm.patchValue({
      id: compra.id,
      fechaCreacion: compra.fechaCreacion,
      numeroFactura: compra.numeroFactura,
      tipoFactura: compra.tipoFactura,
      informacionProovedor: compra.informacionProovedor,
      idProveedor: compra.idProveedor,
      valorFactura: compra.valorFactura,
      valorPagado: compra.valorPagado,
      valorDeuda: compra.valorDeuda,
      estado: compra.estado,
    });
  }

  protected createFromForm(): ICompra {
    return {
      ...new Compra(),
      id: this.editForm.get(['id'])!.value,
      fechaCreacion: this.editForm.get(['fechaCreacion'])!.value,
      numeroFactura: this.editForm.get(['numeroFactura'])!.value,
      tipoFactura: this.editForm.get(['tipoFactura'])!.value,
      informacionProovedor: this.editForm.get(['informacionProovedor'])!.value,
      idProveedor: this.editForm.get(['idProveedor'])!.value,
      valorFactura: this.editForm.get(['valorFactura'])!.value,
      valorPagado: this.editForm.get(['valorPagado'])!.value,
      valorDeuda: this.editForm.get(['valorDeuda'])!.value,
      estado: this.editForm.get(['estado'])!.value,
    };
  }
}
