import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReembolsoComponent } from '../list/reembolso.component';
import { ReembolsoDetailComponent } from '../detail/reembolso-detail.component';
import { ReembolsoUpdateComponent } from '../update/reembolso-update.component';
import { ReembolsoRoutingResolveService } from './reembolso-routing-resolve.service';

const reembolsoRoute: Routes = [
  {
    path: '',
    component: ReembolsoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReembolsoDetailComponent,
    resolve: {
      reembolso: ReembolsoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReembolsoUpdateComponent,
    resolve: {
      reembolso: ReembolsoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReembolsoUpdateComponent,
    resolve: {
      reembolso: ReembolsoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(reembolsoRoute)],
  exports: [RouterModule],
})
export class ReembolsoRoutingModule {}
