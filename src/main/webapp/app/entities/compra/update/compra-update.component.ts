import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICompra, Compra } from '../compra.model';
import { CompraService } from '../service/compra.service';
import { TipoFactura } from 'app/entities/enumerations/tipo-factura.model';
import { ProductoService } from 'app/entities/producto/service/producto.service';
import { IProducto } from 'app/entities/producto/producto.model';
import { IItemFacturaCompra, ItemFacturaCompra } from '../ItemFacturaCompra';
import { ProveedorService } from 'app/entities/proveedor/service/proveedor.service';
import { IProveedor } from 'app/entities/proveedor/proveedor.model';

@Component({
  selector: 'jhi-compra-update',
  templateUrl: './compra-update.component.html',
})
export class CompraUpdateComponent implements OnInit {
  isSaving = false;
  tipoFacturaValues = Object.keys(TipoFactura);
  products?: IProducto[] | null;
  productosSeleccionados?: IItemFacturaCompra[] = [];
  productoSeleccionado?: IItemFacturaCompra | null;
  newProducto = true;
  proveedores?: IProveedor[] | null;

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

  constructor(
    protected compraService: CompraService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder,
    protected productoService: ProductoService,
    protected proveedoresService: ProveedorService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compra }) => {
      this.updateForm(compra);
    });
    this.consultProducts();
    this.getProvedors();
  }

  previousState(): void {
    window.history.back();
  }

  getProvedors(): void {
    this.proveedoresService.query().subscribe({
      next: (res: HttpResponse<IProveedor[]>) => {
        this.proveedores = res.body ?? [];
      },
      error: () => {
        this.proveedores = [];
      },
    });
  }

  addProduct(producto: IProducto): void {
    this.productoSeleccionado = new ItemFacturaCompra();
    this.productoSeleccionado.idProducto = producto.id;
    this.productoSeleccionado.cantidad = Number(1);
    this.productoSeleccionado.precio = this.productoSeleccionado.cantidad * producto.precio!;
    this.productoSeleccionado.nombreProducto = producto.nombre;

    this.products?.forEach(element => {
      if (element.id === producto.id) {
        element.cantidad!++;
      }
    });

    if (this.productosSeleccionados!.length > 0) {
      for (let i = 0; i < this.productosSeleccionados!.length; i++) {
        if (this.productosSeleccionados![i].idProducto === producto.id) {
          this.newProducto = false;
          this.productosSeleccionados![i].cantidad! += Number(1);
          this.productosSeleccionados![i].precio! += Number(producto.precio!.toFixed(0));
          break;
        } else {
          this.newProducto = true;
        }
      }
    }

    if (this.newProducto) {
      this.productosSeleccionados?.push(this.productoSeleccionado);
    }
    this.calculateValues();
  }

  calculateValues(): void {
    let totalInvoice = 0;
    this.productosSeleccionados?.forEach(element => {
      totalInvoice += Number(element.precio);
    });
    this.editForm.get(['valorFactura'])?.setValue(totalInvoice.toFixed(0));
    this.editForm.get(['valorDeuda'])?.setValue(totalInvoice.toFixed(0));
    this.editForm.get(['estado'])?.setValue('Deuda');
  }

  subtractValuesInvoice(): void {
    const invoiceValue = this.editForm.get(['valorFactura'])!.value;
    const paidValue = this.editForm.get(['valorPagado'])!.value;

    const newDebtValue = Number(invoiceValue) - Number(paidValue);

    newDebtValue === 0 ? this.editForm.get(['estado'])?.setValue('Pagada') : this.editForm.get(['estado'])?.setValue('Deuda');

    this.editForm.get(['valorDeuda'])?.setValue(newDebtValue.toFixed(0));
  }

  diminishProduct(producto: IProducto): void {
    if (this.productosSeleccionados) {
      this.productosSeleccionados.forEach(element => {
        if (element.idProducto === producto.id) {
          element.cantidad! -= 1;
          if (element.cantidad === 0) {
            this.deleteProduct(element);
            this.newProducto = true;
            this.deleteProductLis(producto);
          } else if (element.cantidad! >= 0) {
            this.deleteProductLis(producto);
          }
        }
      });
    }
  }

  deleteProductLis(producto: IProducto): void {
    if (this.products) {
      this.products.forEach(element => {
        if (element.id === producto.id) {
          element.cantidad!--;
        }
      });
    }
  }

  deleteProduct(producto: IItemFacturaCompra): void {
    if (this.productosSeleccionados) {
      const index = this.productosSeleccionados.indexOf(producto);
      if (index >= 0) {
        this.productosSeleccionados.splice(index, 1);
      }
    }
  }

  consultProducts(): void {
    this.productoService.allProductos().subscribe({
      next: (res: HttpResponse<IProducto[]>) => {
        this.products = res.body ?? [];

        this.products.forEach(element => {
          element.cantidadOriginal = element.cantidad;
        });
      },
      error: () => {
        this.products = [];
      },
    });
  }

  save(): void {
    this.isSaving = true;
    const compra = this.createFromForm();
    if (compra.id !== undefined) {
      this.subscribeToSaveResponse(this.compraService.update(compra));
    } else {
      if (this.productosSeleccionados) {
        compra.itemsFacturaCompra = this.productosSeleccionados;
      }
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
