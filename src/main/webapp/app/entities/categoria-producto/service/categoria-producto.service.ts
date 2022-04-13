import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICategoriaProducto, getCategoriaProductoIdentifier } from '../categoria-producto.model';

export type EntityResponseType = HttpResponse<ICategoriaProducto>;
export type EntityArrayResponseType = HttpResponse<ICategoriaProducto[]>;

@Injectable({ providedIn: 'root' })
export class CategoriaProductoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/categoria-productos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(categoriaProducto: ICategoriaProducto): Observable<EntityResponseType> {
    return this.http.post<ICategoriaProducto>(this.resourceUrl, categoriaProducto, { observe: 'response' });
  }

  update(categoriaProducto: ICategoriaProducto): Observable<EntityResponseType> {
    return this.http.put<ICategoriaProducto>(
      `${this.resourceUrl}/${getCategoriaProductoIdentifier(categoriaProducto) as number}`,
      categoriaProducto,
      { observe: 'response' }
    );
  }

  partialUpdate(categoriaProducto: ICategoriaProducto): Observable<EntityResponseType> {
    return this.http.patch<ICategoriaProducto>(
      `${this.resourceUrl}/${getCategoriaProductoIdentifier(categoriaProducto) as number}`,
      categoriaProducto,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICategoriaProducto>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICategoriaProducto[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCategoriaProductoToCollectionIfMissing(
    categoriaProductoCollection: ICategoriaProducto[],
    ...categoriaProductosToCheck: (ICategoriaProducto | null | undefined)[]
  ): ICategoriaProducto[] {
    const categoriaProductos: ICategoriaProducto[] = categoriaProductosToCheck.filter(isPresent);
    if (categoriaProductos.length > 0) {
      const categoriaProductoCollectionIdentifiers = categoriaProductoCollection.map(
        categoriaProductoItem => getCategoriaProductoIdentifier(categoriaProductoItem)!
      );
      const categoriaProductosToAdd = categoriaProductos.filter(categoriaProductoItem => {
        const categoriaProductoIdentifier = getCategoriaProductoIdentifier(categoriaProductoItem);
        if (categoriaProductoIdentifier == null || categoriaProductoCollectionIdentifiers.includes(categoriaProductoIdentifier)) {
          return false;
        }
        categoriaProductoCollectionIdentifiers.push(categoriaProductoIdentifier);
        return true;
      });
      return [...categoriaProductosToAdd, ...categoriaProductoCollection];
    }
    return categoriaProductoCollection;
  }
}
