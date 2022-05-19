import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FacturaComponent } from '../list/factura.component';
import { FacturaDetailComponent } from '../detail/factura-detail.component';
import { FacturaUpdateComponent } from '../update/factura-update.component';
import { FacturaRoutingResolveService } from './factura-routing-resolve.service';
import { User2RouteAccessService } from 'app/core/auth/user2-route-access.service';

const facturaRoute: Routes = [
  {
    path: '',
    component: FacturaComponent,
    canActivate: [User2RouteAccessService],
  },
  {
    path: ':id/view',
    component: FacturaDetailComponent,
    resolve: {
      factura: FacturaRoutingResolveService,
    },
    canActivate: [User2RouteAccessService],
  },
  {
    path: 'new',
    component: FacturaUpdateComponent,
    resolve: {
      factura: FacturaRoutingResolveService,
    },
    canActivate: [User2RouteAccessService],
  },
  {
    path: ':id/edit',
    component: FacturaUpdateComponent,
    resolve: {
      factura: FacturaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(facturaRoute)],
  exports: [RouterModule],
})
export class FacturaRoutingModule {}
