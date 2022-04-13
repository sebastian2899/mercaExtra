import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NotificacionComponent } from '../list/notificacion.component';
import { NotificacionDetailComponent } from '../detail/notificacion-detail.component';
import { NotificacionUpdateComponent } from '../update/notificacion-update.component';
import { NotificacionRoutingResolveService } from './notificacion-routing-resolve.service';

const notificacionRoute: Routes = [
  {
    path: '',
    component: NotificacionComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NotificacionDetailComponent,
    resolve: {
      notificacion: NotificacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NotificacionUpdateComponent,
    resolve: {
      notificacion: NotificacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NotificacionUpdateComponent,
    resolve: {
      notificacion: NotificacionRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(notificacionRoute)],
  exports: [RouterModule],
})
export class NotificacionRoutingModule {}
