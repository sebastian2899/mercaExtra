import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDomiciliario } from '../domiciliario.model';

@Component({
  selector: 'jhi-domiciliario-detail',
  templateUrl: './domiciliario-detail.component.html',
})
export class DomiciliarioDetailComponent implements OnInit {
  domiciliario: IDomiciliario | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ domiciliario }) => {
      this.domiciliario = domiciliario;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
