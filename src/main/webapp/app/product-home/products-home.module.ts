import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { PRODUCTS_HOME_ROUTE } from './products-home.route';
import { ProductHomeComponent } from './product-home.component';
import { MatIconModule } from '@angular/material/icon';
import { NgxPaginationModule } from 'ngx-pagination';

@NgModule({
  imports: [SharedModule, MatIconModule, NgxPaginationModule, RouterModule.forChild([PRODUCTS_HOME_ROUTE])],
  declarations: [ProductHomeComponent],
})
export class ProductHomeModule {}
