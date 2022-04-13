import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEgreso } from '../egreso.model';

@Component({
  selector: 'jhi-egreso-detail',
  templateUrl: './egreso-detail.component.html',
})
export class EgresoDetailComponent implements OnInit {
  egreso: IEgreso | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ egreso }) => {
      this.egreso = egreso;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
