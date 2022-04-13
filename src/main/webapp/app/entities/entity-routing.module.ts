import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'pedido',
        data: { pageTitle: 'mercaExtraApp.pedido.home.title' },
        loadChildren: () => import('./pedido/pedido.module').then(m => m.PedidoModule),
      },
      {
        path: 'factura',
        data: { pageTitle: 'mercaExtraApp.factura.home.title' },
        loadChildren: () => import('./factura/factura.module').then(m => m.FacturaModule),
      },
      {
        path: 'notificacion',
        data: { pageTitle: 'mercaExtraApp.notificacion.home.title' },
        loadChildren: () => import('./notificacion/notificacion.module').then(m => m.NotificacionModule),
      },
      {
        path: 'empleado',
        data: { pageTitle: 'mercaExtraApp.empleado.home.title' },
        loadChildren: () => import('./empleado/empleado.module').then(m => m.EmpleadoModule),
      },
      {
        path: 'domiciliario',
        data: { pageTitle: 'mercaExtraApp.domiciliario.home.title' },
        loadChildren: () => import('./domiciliario/domiciliario.module').then(m => m.DomiciliarioModule),
      },
      {
        path: 'caja',
        data: { pageTitle: 'mercaExtraApp.caja.home.title' },
        loadChildren: () => import('./caja/caja.module').then(m => m.CajaModule),
      },
      {
        path: 'categoria-producto',
        data: { pageTitle: 'mercaExtraApp.categoriaProducto.home.title' },
        loadChildren: () => import('./categoria-producto/categoria-producto.module').then(m => m.CategoriaProductoModule),
      },
      {
        path: 'compra',
        data: { pageTitle: 'mercaExtraApp.compra.home.title' },
        loadChildren: () => import('./compra/compra.module').then(m => m.CompraModule),
      },
      {
        path: 'proveedor',
        data: { pageTitle: 'mercaExtraApp.proveedor.home.title' },
        loadChildren: () => import('./proveedor/proveedor.module').then(m => m.ProveedorModule),
      },
      {
        path: 'egreso',
        data: { pageTitle: 'mercaExtraApp.egreso.home.title' },
        loadChildren: () => import('./egreso/egreso.module').then(m => m.EgresoModule),
      },
      {
        path: 'producto',
        data: { pageTitle: 'mercaExtraApp.producto.home.title' },
        loadChildren: () => import('./producto/producto.module').then(m => m.ProductoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
