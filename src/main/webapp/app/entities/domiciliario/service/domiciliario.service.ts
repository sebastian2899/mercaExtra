import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDomiciliario, getDomiciliarioIdentifier } from '../domiciliario.model';

export type EntityResponseType = HttpResponse<IDomiciliario>;
export type EntityArrayResponseType = HttpResponse<IDomiciliario[]>;

@Injectable({ providedIn: 'root' })
export class DomiciliarioService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/domiciliarios');
  protected domiciliariosFiltrosUrl = this.applicationConfigService.getEndpointFor('api/domiciliarios-filtro');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(domiciliario: IDomiciliario): Observable<EntityResponseType> {
    return this.http.post<IDomiciliario>(this.resourceUrl, domiciliario, { observe: 'response' });
  }

  update(domiciliario: IDomiciliario): Observable<EntityResponseType> {
    return this.http.put<IDomiciliario>(`${this.resourceUrl}/${getDomiciliarioIdentifier(domiciliario) as number}`, domiciliario, {
      observe: 'response',
    });
  }

  domiciliariosFiltro(domiciliario: IDomiciliario): Observable<EntityArrayResponseType> {
    return this.http.post<IDomiciliario[]>(this.domiciliariosFiltrosUrl, domiciliario, { observe: 'response' });
  }

  partialUpdate(domiciliario: IDomiciliario): Observable<EntityResponseType> {
    return this.http.patch<IDomiciliario>(`${this.resourceUrl}/${getDomiciliarioIdentifier(domiciliario) as number}`, domiciliario, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDomiciliario>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDomiciliario[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDomiciliarioToCollectionIfMissing(
    domiciliarioCollection: IDomiciliario[],
    ...domiciliariosToCheck: (IDomiciliario | null | undefined)[]
  ): IDomiciliario[] {
    const domiciliarios: IDomiciliario[] = domiciliariosToCheck.filter(isPresent);
    if (domiciliarios.length > 0) {
      const domiciliarioCollectionIdentifiers = domiciliarioCollection.map(
        domiciliarioItem => getDomiciliarioIdentifier(domiciliarioItem)!
      );
      const domiciliariosToAdd = domiciliarios.filter(domiciliarioItem => {
        const domiciliarioIdentifier = getDomiciliarioIdentifier(domiciliarioItem);
        if (domiciliarioIdentifier == null || domiciliarioCollectionIdentifiers.includes(domiciliarioIdentifier)) {
          return false;
        }
        domiciliarioCollectionIdentifiers.push(domiciliarioIdentifier);
        return true;
      });
      return [...domiciliariosToAdd, ...domiciliarioCollection];
    }
    return domiciliarioCollection;
  }
}
