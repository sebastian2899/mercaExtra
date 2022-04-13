import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDomiciliario, Domiciliario } from '../domiciliario.model';
import { DomiciliarioService } from '../service/domiciliario.service';

@Injectable({ providedIn: 'root' })
export class DomiciliarioRoutingResolveService implements Resolve<IDomiciliario> {
  constructor(protected service: DomiciliarioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDomiciliario> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((domiciliario: HttpResponse<Domiciliario>) => {
          if (domiciliario.body) {
            return of(domiciliario.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Domiciliario());
  }
}
