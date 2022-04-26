import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PedidoComponent } from './list/pedido.component';
import { PedidoDetailComponent } from './detail/pedido-detail.component';
import { PedidoUpdateComponent } from './update/pedido-update.component';
import { PedidoDeleteDialogComponent } from './delete/pedido-delete-dialog.component';
import { PedidoRoutingModule } from './route/pedido-routing.module';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  imports: [SharedModule, PedidoRoutingModule, MatIconModule],
  declarations: [PedidoComponent, PedidoDetailComponent, PedidoUpdateComponent, PedidoDeleteDialogComponent],
  entryComponents: [PedidoDeleteDialogComponent],
})
export class PedidoModule {}
