import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompra, getCompraIdentifier } from '../compra.model';

export type EntityResponseType = HttpResponse<ICompra>;
export type EntityArrayResponseType = HttpResponse<ICompra[]>;

@Injectable({ providedIn: 'root' })
export class CompraService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/compras');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(compra: ICompra): Observable<EntityResponseType> {
    return this.http.post<ICompra>(this.resourceUrl, compra, { observe: 'response' });
  }

  update(compra: ICompra): Observable<EntityResponseType> {
    return this.http.put<ICompra>(`${this.resourceUrl}/${getCompraIdentifier(compra) as number}`, compra, { observe: 'response' });
  }

  partialUpdate(compra: ICompra): Observable<EntityResponseType> {
    return this.http.patch<ICompra>(`${this.resourceUrl}/${getCompraIdentifier(compra) as number}`, compra, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICompra>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompra[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCompraToCollectionIfMissing(compraCollection: ICompra[], ...comprasToCheck: (ICompra | null | undefined)[]): ICompra[] {
    const compras: ICompra[] = comprasToCheck.filter(isPresent);
    if (compras.length > 0) {
      const compraCollectionIdentifiers = compraCollection.map(compraItem => getCompraIdentifier(compraItem)!);
      const comprasToAdd = compras.filter(compraItem => {
        const compraIdentifier = getCompraIdentifier(compraItem);
        if (compraIdentifier == null || compraCollectionIdentifiers.includes(compraIdentifier)) {
          return false;
        }
        compraCollectionIdentifiers.push(compraIdentifier);
        return true;
      });
      return [...comprasToAdd, ...compraCollection];
    }
    return compraCollection;
  }
}
