import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CategoriaProductoComponent } from './list/categoria-producto.component';
import { CategoriaProductoDetailComponent } from './detail/categoria-producto-detail.component';
import { CategoriaProductoUpdateComponent } from './update/categoria-producto-update.component';
import { CategoriaProductoDeleteDialogComponent } from './delete/categoria-producto-delete-dialog.component';
import { CategoriaProductoRoutingModule } from './route/categoria-producto-routing.module';

@NgModule({
  imports: [SharedModule, CategoriaProductoRoutingModule],
  declarations: [
    CategoriaProductoComponent,
    CategoriaProductoDetailComponent,
    CategoriaProductoUpdateComponent,
    CategoriaProductoDeleteDialogComponent,
  ],
  entryComponents: [CategoriaProductoDeleteDialogComponent],
})
export class CategoriaProductoModule {}
