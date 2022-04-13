import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICategoriaProducto } from '../categoria-producto.model';

@Component({
  selector: 'jhi-categoria-producto-detail',
  templateUrl: './categoria-producto-detail.component.html',
})
export class CategoriaProductoDetailComponent implements OnInit {
  categoriaProducto: ICategoriaProducto | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ categoriaProducto }) => {
      this.categoriaProducto = categoriaProducto;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
