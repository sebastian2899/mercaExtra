import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IProducto, Producto } from '../producto.model';
import { ProductoService } from '../service/producto.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { CategoriaProductoService } from 'app/entities/categoria-producto/service/categoria-producto.service';
import { ICategoriaProducto } from 'app/entities/categoria-producto/categoria-producto.model';

@Component({
  selector: 'jhi-producto-update',
  templateUrl: './producto-update.component.html',
})
export class ProductoUpdateComponent implements OnInit {
  isSaving = false;
  categorias?: ICategoriaProducto[] | null;
  titulo?: string | null;
  nomCategoria?: string | null;

  editForm = this.fb.group({
    id: [],
    nombre: [],
    cantidad: [],
    descripcion: [],
    categoria: [],
    precio: [],
    valorDescuento: [],
    imagen: [],
    imagenContentType: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected productoService: ProductoService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected categoriaService: CategoriaProductoService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ producto }) => {
      this.updateForm(producto);
      producto.id === null ? (this.titulo = 'Actualizar Producto') : (this.titulo = 'Crear Producto');
    });

    this.cosultarCategoriaProductos();
  }

  cosultarCategoriaProductos(): void {
    this.categoriaService.query().subscribe({
      next: (res: HttpResponse<ICategoriaProducto[]>) => {
        this.categorias = res.body ?? [];
      },
      error: () => {
        this.categorias = [];
      },
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('mercaExtraApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const producto = this.createFromForm();

    if (producto.id !== undefined) {
      this.subscribeToSaveResponse(this.productoService.update(producto));
    } else {
      this.subscribeToSaveResponse(this.productoService.create(producto));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProducto>>): void {
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

  protected updateForm(producto: IProducto): void {
    this.editForm.patchValue({
      id: producto.id,
      nombre: producto.nombre,
      cantidad: producto.cantidad,
      descripcion: producto.descripcion,
      categoria: producto.categoria,
      precio: producto.precio,
      imagen: producto.imagen,
      imagenContentType: producto.imagenContentType,
      valorDescuento: producto.valorDescuento,
    });
  }

  protected createFromForm(): IProducto {
    return {
      ...new Producto(),
      id: this.editForm.get(['id'])!.value,
      nombre: this.editForm.get(['nombre'])!.value,
      cantidad: this.editForm.get(['cantidad'])!.value,
      descripcion: this.editForm.get(['descripcion'])!.value,
      categoria: this.editForm.get(['categoria'])!.value,
      precio: this.editForm.get(['precio'])?.value,
      imagenContentType: this.editForm.get(['imagenContentType'])!.value,
      imagen: this.editForm.get(['imagen'])!.value,
      valorDescuento: this.editForm.get(['valorDescuento'])!.value,
    };
  }
}
