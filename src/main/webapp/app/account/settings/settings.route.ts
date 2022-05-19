import { Route } from '@angular/router';

// import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { User2RouteAccessService } from 'app/core/auth/user2-route-access.service';
import { SettingsComponent } from './settings.component';

export const settingsRoute: Route = {
  path: 'settings',
  component: SettingsComponent,
  data: {
    pageTitle: 'global.menu.account.settings',
  },
  canActivate: [User2RouteAccessService],
};
