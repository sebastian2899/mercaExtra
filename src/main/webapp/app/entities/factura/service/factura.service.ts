import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFactura, getFacturaIdentifier } from '../factura.model';
import { IProducto } from 'app/entities/producto/producto.model';

export type EntityResponseType = HttpResponse<IFactura>;
export type EntityArrayResponseType = HttpResponse<IFactura[]>;
export type ProductoArrayResponseType = HttpResponse<IProducto[]>;

@Injectable({ providedIn: 'root' })
export class FacturaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/facturas');
  protected FacturasUsuarioUrl = this.applicationConfigService.getEndpointFor('api/facturas-usuario');
  protected productosDisponiblesUrl = this.applicationConfigService.getEndpointFor('api/facturas-productos-disponibles');
  protected productosCategoriaUrl = this.applicationConfigService.getEndpointFor('api/factura-productos-categoria');
  protected rePurcharseInvoiceUrl = this.applicationConfigService.getEndpointFor('api/factura-rePurcharse');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(factura: IFactura): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(factura);
    return this.http
      .post<IFactura>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  productosDisponibles(): Observable<ProductoArrayResponseType> {
    return this.http.get<IProducto[]>(this.productosDisponiblesUrl, { observe: 'response' });
  }

  rePurchaseInvoice(factura: IFactura): Observable<EntityResponseType> {
    return this.http
      .post<IFactura>(this.rePurcharseInvoiceUrl, factura, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  facturasUsuario(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IFactura[]>(this.FacturasUsuarioUrl, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  productosCategoria(categoria: string): Observable<ProductoArrayResponseType> {
    return this.http.get<IProducto[]>(`${this.productosCategoriaUrl}/${categoria}`, { observe: 'response' });
  }

  update(factura: IFactura): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(factura);
    return this.http
      .put<IFactura>(`${this.resourceUrl}/${getFacturaIdentifier(factura) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(factura: IFactura): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(factura);
    return this.http
      .patch<IFactura>(`${this.resourceUrl}/${getFacturaIdentifier(factura) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFactura>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFactura[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFacturaToCollectionIfMissing(facturaCollection: IFactura[], ...facturasToCheck: (IFactura | null | undefined)[]): IFactura[] {
    const facturas: IFactura[] = facturasToCheck.filter(isPresent);
    if (facturas.length > 0) {
      const facturaCollectionIdentifiers = facturaCollection.map(facturaItem => getFacturaIdentifier(facturaItem)!);
      const facturasToAdd = facturas.filter(facturaItem => {
        const facturaIdentifier = getFacturaIdentifier(facturaItem);
        if (facturaIdentifier == null || facturaCollectionIdentifiers.includes(facturaIdentifier)) {
          return false;
        }
        facturaCollectionIdentifiers.push(facturaIdentifier);
        return true;
      });
      return [...facturasToAdd, ...facturaCollection];
    }
    return facturaCollection;
  }

  protected convertDateFromClient(factura: IFactura): IFactura {
    return Object.assign({}, factura, {
      fechaCreacion: factura.fechaCreacion?.isValid() ? factura.fechaCreacion.toJSON() : undefined,
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
      res.body.forEach((factura: IFactura) => {
        factura.fechaCreacion = factura.fechaCreacion ? dayjs(factura.fechaCreacion) : undefined;
      });
    }
    return res;
  }
}
