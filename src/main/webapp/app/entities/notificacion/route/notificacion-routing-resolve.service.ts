import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INotificacion, Notificacion } from '../notificacion.model';
import { NotificacionService } from '../service/notificacion.service';

@Injectable({ providedIn: 'root' })
export class NotificacionRoutingResolveService implements Resolve<INotificacion> {
  constructor(protected service: NotificacionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INotificacion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((notificacion: HttpResponse<Notificacion>) => {
          if (notificacion.body) {
            return of(notificacion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Notificacion());
  }
}
