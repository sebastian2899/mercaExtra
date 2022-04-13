import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DomiciliarioComponent } from '../list/domiciliario.component';
import { DomiciliarioDetailComponent } from '../detail/domiciliario-detail.component';
import { DomiciliarioUpdateComponent } from '../update/domiciliario-update.component';
import { DomiciliarioRoutingResolveService } from './domiciliario-routing-resolve.service';

const domiciliarioRoute: Routes = [
  {
    path: '',
    component: DomiciliarioComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DomiciliarioDetailComponent,
    resolve: {
      domiciliario: DomiciliarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DomiciliarioUpdateComponent,
    resolve: {
      domiciliario: DomiciliarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DomiciliarioUpdateComponent,
    resolve: {
      domiciliario: DomiciliarioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(domiciliarioRoute)],
  exports: [RouterModule],
})
export class DomiciliarioRoutingModule {}
