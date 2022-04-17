import { Injectable } from '@angular/core';
import { IProducto } from 'app/entities/producto/producto.model';
import { SessionStorageService } from 'ngx-webstorage';

@Injectable({ providedIn: 'root' })
export class StateStorageService {
  private previousUrlKey = 'previousUrl';
  private productoUrlKey = 'productoKeyUrl';

  constructor(private sessionStorageService: SessionStorageService) {}

  pasoParametroProducto(producto: IProducto): void {
    this.sessionStorageService.store(this.productoUrlKey, producto);
  }

  getParametroProducto(): IProducto | null {
    return this.sessionStorageService.retrieve(this.productoUrlKey) as IProducto | null;
  }

  clearUrlProducto(): void {
    this.sessionStorageService.clear(this.productoUrlKey);
  }

  storeUrl(url: string): void {
    this.sessionStorageService.store(this.previousUrlKey, url);
  }

  getUrl(): string | null {
    return this.sessionStorageService.retrieve(this.previousUrlKey) as string | null;
  }

  clearUrl(): void {
    this.sessionStorageService.clear(this.previousUrlKey);
  }
}
