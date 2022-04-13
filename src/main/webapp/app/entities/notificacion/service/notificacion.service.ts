import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INotificacion, getNotificacionIdentifier } from '../notificacion.model';

export type EntityResponseType = HttpResponse<INotificacion>;
export type EntityArrayResponseType = HttpResponse<INotificacion[]>;

@Injectable({ providedIn: 'root' })
export class NotificacionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/notificacions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(notificacion: INotificacion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificacion);
    return this.http
      .post<INotificacion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(notificacion: INotificacion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificacion);
    return this.http
      .put<INotificacion>(`${this.resourceUrl}/${getNotificacionIdentifier(notificacion) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(notificacion: INotificacion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(notificacion);
    return this.http
      .patch<INotificacion>(`${this.resourceUrl}/${getNotificacionIdentifier(notificacion) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<INotificacion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INotificacion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addNotificacionToCollectionIfMissing(
    notificacionCollection: INotificacion[],
    ...notificacionsToCheck: (INotificacion | null | undefined)[]
  ): INotificacion[] {
    const notificacions: INotificacion[] = notificacionsToCheck.filter(isPresent);
    if (notificacions.length > 0) {
      const notificacionCollectionIdentifiers = notificacionCollection.map(
        notificacionItem => getNotificacionIdentifier(notificacionItem)!
      );
      const notificacionsToAdd = notificacions.filter(notificacionItem => {
        const notificacionIdentifier = getNotificacionIdentifier(notificacionItem);
        if (notificacionIdentifier == null || notificacionCollectionIdentifiers.includes(notificacionIdentifier)) {
          return false;
        }
        notificacionCollectionIdentifiers.push(notificacionIdentifier);
        return true;
      });
      return [...notificacionsToAdd, ...notificacionCollection];
    }
    return notificacionCollection;
  }

  protected convertDateFromClient(notificacion: INotificacion): INotificacion {
    return Object.assign({}, notificacion, {
      fecha: notificacion.fecha?.isValid() ? notificacion.fecha.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fecha = res.body.fecha ? dayjs(res.body.fecha) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((notificacion: INotificacion) => {
        notificacion.fecha = notificacion.fecha ? dayjs(notificacion.fecha) : undefined;
      });
    }
    return res;
  }
}
