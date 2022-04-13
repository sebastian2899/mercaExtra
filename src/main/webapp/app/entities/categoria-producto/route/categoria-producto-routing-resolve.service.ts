import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICategoriaProducto, CategoriaProducto } from '../categoria-producto.model';
import { CategoriaProductoService } from '../service/categoria-producto.service';

@Injectable({ providedIn: 'root' })
export class CategoriaProductoRoutingResolveService implements Resolve<ICategoriaProducto> {
  constructor(protected service: CategoriaProductoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICategoriaProducto> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((categoriaProducto: HttpResponse<CategoriaProducto>) => {
          if (categoriaProducto.body) {
            return of(categoriaProducto.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CategoriaProducto());
  }
}
