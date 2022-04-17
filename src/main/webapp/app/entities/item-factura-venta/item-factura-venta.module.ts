import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ItemFacturaVentaComponent } from './list/item-factura-venta.component';
import { ItemFacturaVentaDetailComponent } from './detail/item-factura-venta-detail.component';
import { ItemFacturaVentaUpdateComponent } from './update/item-factura-venta-update.component';
import { ItemFacturaVentaDeleteDialogComponent } from './delete/item-factura-venta-delete-dialog.component';
import { ItemFacturaVentaRoutingModule } from './route/item-factura-venta-routing.module';

@NgModule({
  imports: [SharedModule, ItemFacturaVentaRoutingModule],
  declarations: [
    ItemFacturaVentaComponent,
    ItemFacturaVentaDetailComponent,
    ItemFacturaVentaUpdateComponent,
    ItemFacturaVentaDeleteDialogComponent,
  ],
  entryComponents: [ItemFacturaVentaDeleteDialogComponent],
})
export class ItemFacturaVentaModule {}
