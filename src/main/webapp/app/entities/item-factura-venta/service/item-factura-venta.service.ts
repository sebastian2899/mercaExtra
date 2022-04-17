import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IItemFacturaVenta, getItemFacturaVentaIdentifier } from '../item-factura-venta.model';

export type EntityResponseType = HttpResponse<IItemFacturaVenta>;
export type EntityArrayResponseType = HttpResponse<IItemFacturaVenta[]>;

@Injectable({ providedIn: 'root' })
export class ItemFacturaVentaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/item-factura-ventas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(itemFacturaVenta: IItemFacturaVenta): Observable<EntityResponseType> {
    return this.http.post<IItemFacturaVenta>(this.resourceUrl, itemFacturaVenta, { observe: 'response' });
  }

  update(itemFacturaVenta: IItemFacturaVenta): Observable<EntityResponseType> {
    return this.http.put<IItemFacturaVenta>(
      `${this.resourceUrl}/${getItemFacturaVentaIdentifier(itemFacturaVenta) as number}`,
      itemFacturaVenta,
      { observe: 'response' }
    );
  }

  partialUpdate(itemFacturaVenta: IItemFacturaVenta): Observable<EntityResponseType> {
    return this.http.patch<IItemFacturaVenta>(
      `${this.resourceUrl}/${getItemFacturaVentaIdentifier(itemFacturaVenta) as number}`,
      itemFacturaVenta,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IItemFacturaVenta>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IItemFacturaVenta[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addItemFacturaVentaToCollectionIfMissing(
    itemFacturaVentaCollection: IItemFacturaVenta[],
    ...itemFacturaVentasToCheck: (IItemFacturaVenta | null | undefined)[]
  ): IItemFacturaVenta[] {
    const itemFacturaVentas: IItemFacturaVenta[] = itemFacturaVentasToCheck.filter(isPresent);
    if (itemFacturaVentas.length > 0) {
      const itemFacturaVentaCollectionIdentifiers = itemFacturaVentaCollection.map(
        itemFacturaVentaItem => getItemFacturaVentaIdentifier(itemFacturaVentaItem)!
      );
      const itemFacturaVentasToAdd = itemFacturaVentas.filter(itemFacturaVentaItem => {
        const itemFacturaVentaIdentifier = getItemFacturaVentaIdentifier(itemFacturaVentaItem);
        if (itemFacturaVentaIdentifier == null || itemFacturaVentaCollectionIdentifiers.includes(itemFacturaVentaIdentifier)) {
          return false;
        }
        itemFacturaVentaCollectionIdentifiers.push(itemFacturaVentaIdentifier);
        return true;
      });
      return [...itemFacturaVentasToAdd, ...itemFacturaVentaCollection];
    }
    return itemFacturaVentaCollection;
  }
}
