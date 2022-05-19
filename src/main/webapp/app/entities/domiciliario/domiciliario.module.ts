import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DomiciliarioComponent } from './list/domiciliario.component';
import { DomiciliarioDetailComponent } from './detail/domiciliario-detail.component';
import { DomiciliarioUpdateComponent } from './update/domiciliario-update.component';
import { DomiciliarioDeleteDialogComponent } from './delete/domiciliario-delete-dialog.component';
import { DomiciliarioRoutingModule } from './route/domiciliario-routing.module';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  imports: [SharedModule, DomiciliarioRoutingModule, MatIconModule],
  declarations: [DomiciliarioComponent, DomiciliarioDetailComponent, DomiciliarioUpdateComponent, DomiciliarioDeleteDialogComponent],
  entryComponents: [DomiciliarioDeleteDialogComponent],
})
export class DomiciliarioModule {}
