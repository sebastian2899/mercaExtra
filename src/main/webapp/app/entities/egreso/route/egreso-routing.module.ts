import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EgresoComponent } from '../list/egreso.component';
import { EgresoDetailComponent } from '../detail/egreso-detail.component';
import { EgresoUpdateComponent } from '../update/egreso-update.component';
import { EgresoRoutingResolveService } from './egreso-routing-resolve.service';

const egresoRoute: Routes = [
  {
    path: '',
    component: EgresoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EgresoDetailComponent,
    resolve: {
      egreso: EgresoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EgresoUpdateComponent,
    resolve: {
      egreso: EgresoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EgresoUpdateComponent,
    resolve: {
      egreso: EgresoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(egresoRoute)],
  exports: [RouterModule],
})
export class EgresoRoutingModule {}
