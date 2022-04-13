import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProveedor, Proveedor } from '../proveedor.model';
import { ProveedorService } from '../service/proveedor.service';

@Injectable({ providedIn: 'root' })
export class ProveedorRoutingResolveService implements Resolve<IProveedor> {
  constructor(protected service: ProveedorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProveedor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((proveedor: HttpResponse<Proveedor>) => {
          if (proveedor.body) {
            return of(proveedor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Proveedor());
  }
}
