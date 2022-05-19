import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProducto, getProductoIdentifier } from '../producto.model';

export type EntityResponseType = HttpResponse<IProducto>;
export type EntityArrayResponseType = HttpResponse<IProducto[]>;

@Injectable({ providedIn: 'root' })
export class ProductoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/productos');
  protected resourceLogoutUrl = this.applicationConfigService.getEndpointFor('api/logout-productos');
  protected productosAgotadosUrl = this.applicationConfigService.getEndpointFor('api/productos-agotados');
  protected productosEscasosUrl = this.applicationConfigService.getEndpointFor('api/productos-enEscases');
  protected productosCategoriaUrl = this.applicationConfigService.getEndpointFor('api/productos-categoria');
  protected productosFiltroUrl = this.applicationConfigService.getEndpointFor('api/producto-filtros');
  protected productosCategoriaFiltroUrl = this.applicationConfigService.getEndpointFor('api/productos-filtros-categoria');
  protected aplicarPorcentajeProductosUrl = this.applicationConfigService.getEndpointFor('api/producto-porcentaje');
  protected getSimilarProductsURL = this.applicationConfigService.getEndpointFor('api/productos-similares');
  protected getAllProductsURL = this.applicationConfigService.getEndpointFor('api/productos/all');
  protected getdisscountProductsURL = this.applicationConfigService.getEndpointFor('api/discount-products-aviable');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(producto: IProducto): Observable<EntityResponseType> {
    return this.http.post<IProducto>(this.resourceUrl, producto, { observe: 'response' });
  }

  aplicarPorcentajeProducto(opcion: string, cantidad: number): Observable<HttpResponse<{}>> {
    return this.http.get(`${this.aplicarPorcentajeProductosUrl}/${opcion}/${cantidad}`, { observe: 'response' });
  }

  getSimilarProductos(producto: IProducto): Observable<EntityArrayResponseType> {
    return this.http.post<IProducto[]>(this.getSimilarProductsURL, producto, { observe: 'response' });
  }

  allProductos(): Observable<EntityArrayResponseType> {
    return this.http.get<IProducto[]>(this.getAllProductsURL, { observe: 'response' });
  }

  getDiscountProduts(): Observable<EntityArrayResponseType> {
    return this.http.get<IProducto[]>(this.getdisscountProductsURL, { observe: 'response' });
  }

  productosCategoriaFiltro(opcion: number, categoria: string): Observable<EntityArrayResponseType> {
    let params = null;
    if (categoria) {
      params = new HttpParams().set('categoria', categoria.toString());
    }
    return this.http.post<IProducto[]>(`${this.productosCategoriaFiltroUrl}/${opcion}`, params!, { observe: 'response' });
  }

  productosCategoria(categoria: string): Observable<EntityArrayResponseType> {
    return this.http.get<IProducto[]>(`${this.productosCategoriaUrl}/${categoria}`, { observe: 'response' });
  }

  productosFiltro(producto: IProducto): Observable<EntityArrayResponseType> {
    return this.http.post<IProducto[]>(this.productosFiltroUrl, producto, { observe: 'response' });
  }

  update(producto: IProducto): Observable<EntityResponseType> {
    return this.http.put<IProducto>(`${this.resourceUrl}/${getProductoIdentifier(producto) as number}`, producto, { observe: 'response' });
  }

  productosAgotados(): Observable<EntityArrayResponseType> {
    return this.http.get<IProducto[]>(this.productosAgotadosUrl, { observe: 'response' });
  }

  productosEscasos(): Observable<EntityArrayResponseType> {
    return this.http.get<IProducto[]>(this.productosEscasosUrl, { observe: 'response' });
  }

  partialUpdate(producto: IProducto): Observable<EntityResponseType> {
    return this.http.patch<IProducto>(`${this.resourceUrl}/${getProductoIdentifier(producto) as number}`, producto, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProducto>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProducto[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  queryLogout(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProducto[]>(this.resourceLogoutUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProductoToCollectionIfMissing(productoCollection: IProducto[], ...productosToCheck: (IProducto | null | undefined)[]): IProducto[] {
    const productos: IProducto[] = productosToCheck.filter(isPresent);
    if (productos.length > 0) {
      const productoCollectionIdentifiers = productoCollection.map(productoItem => getProductoIdentifier(productoItem)!);
      const productosToAdd = productos.filter(productoItem => {
        const productoIdentifier = getProductoIdentifier(productoItem);
        if (productoIdentifier == null || productoCollectionIdentifiers.includes(productoIdentifier)) {
          return false;
        }
        productoCollectionIdentifiers.push(productoIdentifier);
        return true;
      });
      return [...productosToAdd, ...productoCollection];
    }
    return productoCollection;
  }
}
