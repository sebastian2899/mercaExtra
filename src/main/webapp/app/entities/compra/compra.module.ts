import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CompraComponent } from './list/compra.component';
import { CompraDetailComponent } from './detail/compra-detail.component';
import { CompraUpdateComponent } from './update/compra-update.component';
import { CompraDeleteDialogComponent } from './delete/compra-delete-dialog.component';
import { CompraRoutingModule } from './route/compra-routing.module';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  imports: [SharedModule, CompraRoutingModule, MatIconModule],
  declarations: [CompraComponent, CompraDetailComponent, CompraUpdateComponent, CompraDeleteDialogComponent],
  entryComponents: [CompraDeleteDialogComponent],
})
export class CompraModule {}
