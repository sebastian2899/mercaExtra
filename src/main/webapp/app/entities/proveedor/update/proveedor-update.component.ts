import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IProveedor, Proveedor } from '../proveedor.model';
import { ProveedorService } from '../service/proveedor.service';
import { TipoDoc } from 'app/entities/enumerations/tipo-doc.model';
import { TipoProveedor } from 'app/entities/enumerations/tipo-proveedor.model';

@Component({
  selector: 'jhi-proveedor-update',
  templateUrl: './proveedor-update.component.html',
})
export class ProveedorUpdateComponent implements OnInit {
  isSaving = false;
  tipoDocValues = Object.keys(TipoDoc);
  tipoProveedorValues = Object.keys(TipoProveedor);

  editForm = this.fb.group({
    id: [],
    nombre: [],
    apellido: [],
    tipoCC: [],
    numeroCC: [],
    numCelular: [],
    email: [],
    tipoProovedor: [],
  });

  constructor(protected proveedorService: ProveedorService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proveedor }) => {
      this.updateForm(proveedor);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const proveedor = this.createFromForm();
    if (proveedor.id !== undefined) {
      this.subscribeToSaveResponse(this.proveedorService.update(proveedor));
    } else {
      this.subscribeToSaveResponse(this.proveedorService.create(proveedor));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProveedor>>): void {
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

  protected updateForm(proveedor: IProveedor): void {
    this.editForm.patchValue({
      id: proveedor.id,
      nombre: proveedor.nombre,
      apellido: proveedor.apellido,
      tipoCC: proveedor.tipoCC,
      numeroCC: proveedor.numeroCC,
      numCelular: proveedor.numCelular,
      email: proveedor.email,
      tipoProovedor: proveedor.tipoProovedor,
    });
  }

  protected createFromForm(): IProveedor {
    return {
      ...new Proveedor(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      apellido: this.editForm.get(['apellido'])!.value,
      tipoCC: this.editForm.get(['tipoCC'])!.value,
      numeroCC: this.editForm.get(['numeroCC'])!.value,
      numCelular: this.editForm.get(['numCelular'])!.value,
      email: this.editForm.get(['email'])!.value,
      tipoProovedor: this.editForm.get(['tipoProovedor'])!.value,
    };
  }
}
