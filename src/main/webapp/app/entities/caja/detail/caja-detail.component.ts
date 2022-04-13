import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICaja } from '../caja.model';

@Component({
  selector: 'jhi-caja-detail',
  templateUrl: './caja-detail.component.html',
})
export class CajaDetailComponent implements OnInit {
  caja: ICaja | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ caja }) => {
      this.caja = caja;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
