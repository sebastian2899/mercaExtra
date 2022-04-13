import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProveedor, getProveedorIdentifier } from '../proveedor.model';

export type EntityResponseType = HttpResponse<IProveedor>;
export type EntityArrayResponseType = HttpResponse<IProveedor[]>;

@Injectable({ providedIn: 'root' })
export class ProveedorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/proveedors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(proveedor: IProveedor): Observable<EntityResponseType> {
    return this.http.post<IProveedor>(this.resourceUrl, proveedor, { observe: 'response' });
  }

  update(proveedor: IProveedor): Observable<EntityResponseType> {
    return this.http.put<IProveedor>(`${this.resourceUrl}/${getProveedorIdentifier(proveedor) as number}`, proveedor, {
      observe: 'response',
    });
  }

  partialUpdate(proveedor: IProveedor): Observable<EntityResponseType> {
    return this.http.patch<IProveedor>(`${this.resourceUrl}/${getProveedorIdentifier(proveedor) as number}`, proveedor, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProveedor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProveedor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProveedorToCollectionIfMissing(
    proveedorCollection: IProveedor[],
    ...proveedorsToCheck: (IProveedor | null | undefined)[]
  ): IProveedor[] {
    const proveedors: IProveedor[] = proveedorsToCheck.filter(isPresent);
    if (proveedors.length > 0) {
      const proveedorCollectionIdentifiers = proveedorCollection.map(proveedorItem => getProveedorIdentifier(proveedorItem)!);
      const proveedorsToAdd = proveedors.filter(proveedorItem => {
        const proveedorIdentifier = getProveedorIdentifier(proveedorItem);
        if (proveedorIdentifier == null || proveedorCollectionIdentifiers.includes(proveedorIdentifier)) {
          return false;
        }
        proveedorCollectionIdentifiers.push(proveedorIdentifier);
        return true;
      });
      return [...proveedorsToAdd, ...proveedorCollection];
    }
    return proveedorCollection;
  }
}
