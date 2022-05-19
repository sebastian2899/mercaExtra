import { Route } from '@angular/router';
// import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { User2RouteAccessService } from 'app/core/auth/user2-route-access.service';
import { PasswordComponent } from './password.component';

export const passwordRoute: Route = {
  path: 'password',
  component: PasswordComponent,
  data: {
    pageTitle: 'global.menu.account.password',
  },
  canActivate: [User2RouteAccessService],
};
