import { Route } from '@angular/router';

import { ProductHomeComponent } from './product-home.component';

export const PRODUCTS_HOME_ROUTE: Route = {
  path: '',
  component: ProductHomeComponent,
  data: {
    pageTitle: 'productsHome',
  },
};
