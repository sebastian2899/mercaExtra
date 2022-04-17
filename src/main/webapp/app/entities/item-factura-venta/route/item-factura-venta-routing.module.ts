import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ItemFacturaVentaComponent } from '../list/item-factura-venta.component';
import { ItemFacturaVentaDetailComponent } from '../detail/item-factura-venta-detail.component';
import { ItemFacturaVentaUpdateComponent } from '../update/item-factura-venta-update.component';
import { ItemFacturaVentaRoutingResolveService } from './item-factura-venta-routing-resolve.service';

const itemFacturaVentaRoute: Routes = [
  {
    path: '',
    component: ItemFacturaVentaComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ItemFacturaVentaDetailComponent,
    resolve: {
      itemFacturaVenta: ItemFacturaVentaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ItemFacturaVentaUpdateComponent,
    resolve: {
      itemFacturaVenta: ItemFacturaVentaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ItemFacturaVentaUpdateComponent,
    resolve: {
      itemFacturaVenta: ItemFacturaVentaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(itemFacturaVentaRoute)],
  exports: [RouterModule],
})
export class ItemFacturaVentaRoutingModule {}
