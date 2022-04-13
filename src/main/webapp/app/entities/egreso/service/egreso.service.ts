import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEgreso, getEgresoIdentifier } from '../egreso.model';

export type EntityResponseType = HttpResponse<IEgreso>;
export type EntityArrayResponseType = HttpResponse<IEgreso[]>;

@Injectable({ providedIn: 'root' })
export class EgresoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/egresos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(egreso: IEgreso): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(egreso);
    return this.http
      .post<IEgreso>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(egreso: IEgreso): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(egreso);
    return this.http
      .put<IEgreso>(`${this.resourceUrl}/${getEgresoIdentifier(egreso) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(egreso: IEgreso): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(egreso);
    return this.http
      .patch<IEgreso>(`${this.resourceUrl}/${getEgresoIdentifier(egreso) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEgreso>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEgreso[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEgresoToCollectionIfMissing(egresoCollection: IEgreso[], ...egresosToCheck: (IEgreso | null | undefined)[]): IEgreso[] {
    const egresos: IEgreso[] = egresosToCheck.filter(isPresent);
    if (egresos.length > 0) {
      const egresoCollectionIdentifiers = egresoCollection.map(egresoItem => getEgresoIdentifier(egresoItem)!);
      const egresosToAdd = egresos.filter(egresoItem => {
        const egresoIdentifier = getEgresoIdentifier(egresoItem);
        if (egresoIdentifier == null || egresoCollectionIdentifiers.includes(egresoIdentifier)) {
          return false;
        }
        egresoCollectionIdentifiers.push(egresoIdentifier);
        return true;
      });
      return [...egresosToAdd, ...egresoCollection];
    }
    return egresoCollection;
  }

  protected convertDateFromClient(egreso: IEgreso): IEgreso {
    return Object.assign({}, egreso, {
      fechaCreacion: egreso.fechaCreacion?.isValid() ? egreso.fechaCreacion.toJSON() : undefined,
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
      res.body.forEach((egreso: IEgreso) => {
        egreso.fechaCreacion = egreso.fechaCreacion ? dayjs(egreso.fechaCreacion) : undefined;
      });
    }
    return res;
  }
}
