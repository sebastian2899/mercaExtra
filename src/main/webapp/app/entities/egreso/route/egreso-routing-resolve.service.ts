import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEgreso, Egreso } from '../egreso.model';
import { EgresoService } from '../service/egreso.service';

@Injectable({ providedIn: 'root' })
export class EgresoRoutingResolveService implements Resolve<IEgreso> {
  constructor(protected service: EgresoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEgreso> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((egreso: HttpResponse<Egreso>) => {
          if (egreso.body) {
            return of(egreso.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Egreso());
  }
}
