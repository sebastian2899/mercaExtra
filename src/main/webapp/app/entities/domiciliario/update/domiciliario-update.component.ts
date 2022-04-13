import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDomiciliario, Domiciliario } from '../domiciliario.model';
import { DomiciliarioService } from '../service/domiciliario.service';
import { TipoSalario } from 'app/entities/enumerations/tipo-salario.model';
import { EstadoDomiciliario } from 'app/entities/enumerations/estado-domiciliario.model';

@Component({
  selector: 'jhi-domiciliario-update',
  templateUrl: './domiciliario-update.component.html',
})
export class DomiciliarioUpdateComponent implements OnInit {
  isSaving = false;
  tipoSalarioValues = Object.keys(TipoSalario);
  estadoDomiciliarioValues = Object.keys(EstadoDomiciliario);

  editForm = this.fb.group({
    id: [],
    nombre: [],
    apellido: [],
    salario: [],
    telefono: [],
    horario: [],
    sueldo: [],
    estado: [],
  });

  constructor(protected domiciliarioService: DomiciliarioService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ domiciliario }) => {
      this.updateForm(domiciliario);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const domiciliario = this.createFromForm();
    if (domiciliario.id !== undefined) {
      this.subscribeToSaveResponse(this.domiciliarioService.update(domiciliario));
    } else {
      this.subscribeToSaveResponse(this.domiciliarioService.create(domiciliario));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDomiciliario>>): void {
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

  protected updateForm(domiciliario: IDomiciliario): void {
    this.editForm.patchValue({
      id: domiciliario.id,
      nombre: domiciliario.nombre,
      apellido: domiciliario.apellido,
      salario: domiciliario.salario,
      telefono: domiciliario.telefono,
      horario: domiciliario.horario,
      sueldo: domiciliario.sueldo,
      estado: domiciliario.estado,
    });
  }

  protected createFromForm(): IDomiciliario {
    return {
      ...new Domiciliario(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      apellido: this.editForm.get(['apellido'])!.value,
      salario: this.editForm.get(['salario'])!.value,
      telefono: this.editForm.get(['telefono'])!.value,
      horario: this.editForm.get(['horario'])!.value,
      sueldo: this.editForm.get(['sueldo'])!.value,
      estado: this.editForm.get(['estado'])!.value,
    };
  }
}
