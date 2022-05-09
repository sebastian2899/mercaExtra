import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPedido, getPedidoIdentifier } from '../pedido.model';
import { IFacturaPedido } from '../factura-pedido';

export type EntityResponseType = HttpResponse<IPedido>;
export type EntityArrayResponseType = HttpResponse<IPedido[]>;
export type FacturaPedidoResponseType = HttpResponse<IFacturaPedido[]>;
export type BooleanResponseType = HttpResponse<boolean>;
export type NumberResponseType = HttpResponse<number>;

@Injectable({ providedIn: 'root' })
export class PedidoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pedidos');
  protected facturasUrl = this.applicationConfigService.getEndpointFor('api/pedidos-facturas');
  protected aviableDomiciliaryUrl = this.applicationConfigService.getEndpointFor('api/pedido-validate-domiciliary');
  protected pedidoCommingUrl = this.applicationConfigService.getEndpointFor('api/pedido-comming');
  protected pedidoFinalizadoUrl = this.applicationConfigService.getEndpointFor('api/pedido-finalizado');
  protected pedidosFechaUrl = this.applicationConfigService.getEndpointFor('api/pedidos-fecha');
  protected resourceReembolsoPedidosUrl = this.applicationConfigService.getEndpointFor('api/reembolsos-pedidos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pedido: IPedido): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pedido);
    return this.http
      .post<IPedido>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  pedidosFecha(fecha: string): Observable<EntityArrayResponseType> {
    return this.http
      .get<IPedido[]>(`${this.pedidosFechaUrl}/${fecha.toString()}`, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  reembolsosPedidos(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IPedido[]>(this.resourceReembolsoPedidosUrl, { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  facturasPedido(): Observable<FacturaPedidoResponseType> {
    return this.http.get<IFacturaPedido[]>(this.facturasUrl, { observe: 'response' });
  }

  pedidoInComming(): Observable<EntityResponseType> {
    return this.http.get<IPedido>(this.pedidoCommingUrl, { observe: 'response' });
  }

  pedidoFinalizado(pedido: IPedido): Observable<HttpResponse<{}>> {
    return this.http.post(this.pedidoFinalizadoUrl, pedido, { observe: 'response' });
  }

  aviableDomiciliary(): Observable<NumberResponseType> {
    return this.http.get<number>(this.aviableDomiciliaryUrl, { observe: 'response' });
  }

  update(pedido: IPedido): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pedido);
    return this.http
      .put<IPedido>(`${this.resourceUrl}/${getPedidoIdentifier(pedido) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(pedido: IPedido): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pedido);
    return this.http
      .patch<IPedido>(`${this.resourceUrl}/${getPedidoIdentifier(pedido) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPedido>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPedido[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPedidoToCollectionIfMissing(pedidoCollection: IPedido[], ...pedidosToCheck: (IPedido | null | undefined)[]): IPedido[] {
    const pedidos: IPedido[] = pedidosToCheck.filter(isPresent);
    if (pedidos.length > 0) {
      const pedidoCollectionIdentifiers = pedidoCollection.map(pedidoItem => getPedidoIdentifier(pedidoItem)!);
      const pedidosToAdd = pedidos.filter(pedidoItem => {
        const pedidoIdentifier = getPedidoIdentifier(pedidoItem);
        if (pedidoIdentifier == null || pedidoCollectionIdentifiers.includes(pedidoIdentifier)) {
          return false;
        }
        pedidoCollectionIdentifiers.push(pedidoIdentifier);
        return true;
      });
      return [...pedidosToAdd, ...pedidoCollection];
    }
    return pedidoCollection;
  }

  protected convertDateFromClient(pedido: IPedido): IPedido {
    return Object.assign({}, pedido, {
      fechaPedido: pedido.fechaPedido?.isValid() ? pedido.fechaPedido.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaPedido = res.body.fechaPedido ? dayjs(res.body.fechaPedido) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((pedido: IPedido) => {
        pedido.fechaPedido = pedido.fechaPedido ? dayjs(pedido.fechaPedido) : undefined;
      });
    }
    return res;
  }
}
