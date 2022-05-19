import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompra, getCompraIdentifier } from '../compra.model';
import dayjs from 'dayjs/esm';

export type EntityResponseType = HttpResponse<ICompra>;
export type EntityArrayResponseType = HttpResponse<ICompra[]>;

@Injectable({ providedIn: 'root' })
export class CompraService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/compras');
  protected compraFiltersUrl = this.applicationConfigService.getEndpointFor('api/compras/filtros');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(compra: ICompra): Observable<EntityResponseType> {
    return this.http
      .post<ICompra>(this.resourceUrl, compra, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  compraFilters(compra: ICompra, fecha: string): Observable<EntityArrayResponseType> {
    return this.http
      .post<ICompra[]>(`${this.compraFiltersUrl}/${fecha}`, compra, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  update(compra: ICompra): Observable<EntityResponseType> {
    return this.http
      .put<ICompra>(`${this.resourceUrl}/${getCompraIdentifier(compra) as number}`, compra, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(compra: ICompra): Observable<EntityResponseType> {
    return this.http
      .patch<ICompra>(`${this.resourceUrl}/${getCompraIdentifier(compra) as number}`, compra, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICompra>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICompra[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
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

  protected convertDateFromClient(compra: ICompra): ICompra {
    return Object.assign({}, compra, {
      fechaCreacion: compra.fechaCreacion?.isValid() ? compra.fechaCreacion.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaCreacion = res.body.fechaCreacion ? dayjs(res.body.fechaCreacion) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((compra: ICompra) => {
        compra.fechaCreacion = compra.fechaCreacion ? dayjs(compra.fechaCreacion) : undefined;
      });
    }
    return res;
  }
}
