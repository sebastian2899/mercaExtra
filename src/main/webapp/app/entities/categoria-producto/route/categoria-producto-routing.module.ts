import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CategoriaProductoComponent } from '../list/categoria-producto.component';
import { CategoriaProductoDetailComponent } from '../detail/categoria-producto-detail.component';
import { CategoriaProductoUpdateComponent } from '../update/categoria-producto-update.component';
import { CategoriaProductoRoutingResolveService } from './categoria-producto-routing-resolve.service';

const categoriaProductoRoute: Routes = [
  {
    path: '',
    component: CategoriaProductoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CategoriaProductoDetailComponent,
    resolve: {
      categoriaProducto: CategoriaProductoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CategoriaProductoUpdateComponent,
    resolve: {
      categoriaProducto: CategoriaProductoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CategoriaProductoUpdateComponent,
    resolve: {
      categoriaProducto: CategoriaProductoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(categoriaProductoRoute)],
  exports: [RouterModule],
})
export class CategoriaProductoRoutingModule {}
