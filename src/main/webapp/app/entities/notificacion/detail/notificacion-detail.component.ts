import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INotificacion } from '../notificacion.model';

@Component({
  selector: 'jhi-notificacion-detail',
  templateUrl: './notificacion-detail.component.html',
})
export class NotificacionDetailComponent implements OnInit {
  notificacion: INotificacion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notificacion }) => {
      this.notificacion = notificacion;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
