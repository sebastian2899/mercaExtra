import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReembolso } from '../reembolso.model';

@Component({
  selector: 'jhi-reembolso-detail',
  templateUrl: './reembolso-detail.component.html',
})
export class ReembolsoDetailComponent implements OnInit {
  reembolso: IReembolso | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reembolso }) => {
      this.reembolso = reembolso;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
