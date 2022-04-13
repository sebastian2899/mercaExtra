import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IEmpleado, Empleado } from '../empleado.model';
import { EmpleadoService } from '../service/empleado.service';

@Component({
  selector: 'jhi-empleado-update',
  templateUrl: './empleado-update.component.html',
})
export class EmpleadoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombre: [],
    apellido: [],
    email: [],
    numCelular: [],
    direccion: [],
    salario: [],
    horario: [],
  });

  constructor(protected empleadoService: EmpleadoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ empleado }) => {
      if (empleado.id === undefined) {
        const today = dayjs().startOf('day');
        empleado.direccion = today;
      }

      this.updateForm(empleado);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const empleado = this.createFromForm();
    if (empleado.id !== undefined) {
      this.subscribeToSaveResponse(this.empleadoService.update(empleado));
    } else {
      this.subscribeToSaveResponse(this.empleadoService.create(empleado));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmpleado>>): void {
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

  protected updateForm(empleado: IEmpleado): void {
    this.editForm.patchValue({
      id: empleado.id,
      nombre: empleado.nombre,
      apellido: empleado.apellido,
      email: empleado.email,
      numCelular: empleado.numCelular,
      direccion: empleado.direccion ? empleado.direccion.format(DATE_TIME_FORMAT) : null,
      salario: empleado.salario,
      horario: empleado.horario,
    });
  }

  protected createFromForm(): IEmpleado {
    return {
      ...new Empleado(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      apellido: this.editForm.get(['apellido'])!.value,
      email: this.editForm.get(['email'])!.value,
      numCelular: this.editForm.get(['numCelular'])!.value,
      direccion: this.editForm.get(['direccion'])!.value ? dayjs(this.editForm.get(['direccion'])!.value, DATE_TIME_FORMAT) : undefined,
      salario: this.editForm.get(['salario'])!.value,
      horario: this.editForm.get(['horario'])!.value,
    };
  }
}
