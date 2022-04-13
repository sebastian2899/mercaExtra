import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NotificacionComponent } from './list/notificacion.component';
import { NotificacionDetailComponent } from './detail/notificacion-detail.component';
import { NotificacionUpdateComponent } from './update/notificacion-update.component';
import { NotificacionDeleteDialogComponent } from './delete/notificacion-delete-dialog.component';
import { NotificacionRoutingModule } from './route/notificacion-routing.module';

@NgModule({
  imports: [SharedModule, NotificacionRoutingModule],
  declarations: [NotificacionComponent, NotificacionDetailComponent, NotificacionUpdateComponent, NotificacionDeleteDialogComponent],
  entryComponents: [NotificacionDeleteDialogComponent],
})
export class NotificacionModule {}
