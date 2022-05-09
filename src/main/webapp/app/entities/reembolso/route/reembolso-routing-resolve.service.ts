import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReembolso, Reembolso } from '../reembolso.model';
import { ReembolsoService } from '../service/reembolso.service';

@Injectable({ providedIn: 'root' })
export class ReembolsoRoutingResolveService implements Resolve<IReembolso> {
  constructor(protected service: ReembolsoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReembolso> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((reembolso: HttpResponse<Reembolso>) => {
          if (reembolso.body) {
            return of(reembolso.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Reembolso());
  }
}
