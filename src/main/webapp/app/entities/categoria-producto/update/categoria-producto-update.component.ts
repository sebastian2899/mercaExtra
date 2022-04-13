import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICategoriaProducto, CategoriaProducto } from '../categoria-producto.model';
import { CategoriaProductoService } from '../service/categoria-producto.service';

@Component({
  selector: 'jhi-categoria-producto-update',
  templateUrl: './categoria-producto-update.component.html',
})
export class CategoriaProductoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nombreCategoria: [],
    descripcion: [],
  });

  constructor(
    protected categoriaProductoService: CategoriaProductoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categoriaProducto }) => {
      this.updateForm(categoriaProducto);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const categoriaProducto = this.createFromForm();
    if (categoriaProducto.id !== undefined) {
      this.subscribeToSaveResponse(this.categoriaProductoService.update(categoriaProducto));
    } else {
      this.subscribeToSaveResponse(this.categoriaProductoService.create(categoriaProducto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICategoriaProducto>>): void {
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

  protected updateForm(categoriaProducto: ICategoriaProducto): void {
    this.editForm.patchValue({
      id: categoriaProducto.id,
      nombreCategoria: categoriaProducto.nombreCategoria,
      descripcion: categoriaProducto.descripcion,
    });
  }

  protected createFromForm(): ICategoriaProducto {
    return {
      ...new CategoriaProducto(),
      id: this.editForm.get(['id'])!.value,
      nombreCategoria: this.editForm.get(['nombreCategoria'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
    };
  }
}
