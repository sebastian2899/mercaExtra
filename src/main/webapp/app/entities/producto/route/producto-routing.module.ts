import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductoComponent } from '../list/producto.component';
import { ProductoDetailComponent } from '../detail/producto-detail.component';
import { ProductoUpdateComponent } from '../update/producto-update.component';
import { ProductoRoutingResolveService } from './producto-routing-resolve.service';
import { UserRouteAccessService2 } from 'app/core/auth/user-route-access.service2';

const productoRoute: Routes = [
  {
    path: '',
    component: ProductoComponent,
    canActivate: [UserRouteAccessService2],
  },
  {
    path: ':id/view',
    component: ProductoDetailComponent,
    resolve: {
      producto: ProductoRoutingResolveService,
    },
    // canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductoUpdateComponent,
    resolve: {
      producto: ProductoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductoUpdateComponent,
    resolve: {
      producto: ProductoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productoRoute)],
  exports: [RouterModule],
})
export class ProductoRoutingModule {}
