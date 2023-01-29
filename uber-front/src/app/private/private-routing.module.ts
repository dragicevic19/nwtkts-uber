import { Component, NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../core/guards/auth/auth.guard';
import { RoleGuard } from '../core/guards/role/role.guard';
import { ActiveRidesComponent } from './components/driver/active-rides/active-rides.component';
import { AcceptSplitFareComponent } from './pages/client/accept-split-fare/accept-split-fare.component';
import { ClientTokensComponent } from './pages/client/client-tokens/client-tokens.component';
import { DriverHomepageComponent } from './pages/driver/driver-homepage/driver-homepage.component';
import { HomepageComponent } from './pages/client/homepage/homepage.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { PrivateComponent } from './private.component';
import { RideHistoryComponent } from './pages/ride-history/ride-history.component';
import { ChatComponent } from './pages/chat/chat.component';
import { AdminSupportComponent } from './pages/administrator/admin-support/admin-support.component';
import { FavRoutesComponent } from './pages/client/fav-routes/fav-routes.component';

import { AllUsersComponent } from './components/administrator/all-users/all-users/all-users.component';
import { AdminAddDriverComponent } from './components/administrator/admin-add-driver/admin-add-driver.component';
import { AdminNotificationsComponent } from './pages/administrator/admin-notifications/admin-notifications.component';
import { ChartComponent } from './components/chart/chart.component';
import { ReportsComponent } from './pages/reports/reports.component';

const routes: Routes = [
  {
    path: 'uber',
    component: PrivateComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: '',
        component: HomepageComponent,
        canActivate: [RoleGuard],
        data: { expectedRoles: 'ROLE_CLIENT' },
      },
      { path: 'profile', component: ProfileComponent },
      {
        path: 'wallet',
        component: ClientTokensComponent,
        canActivate: [RoleGuard],
        data: { expectedRoles: 'ROLE_CLIENT' },
      },
      {
        path: 'split-fare',
        component: AcceptSplitFareComponent,
        canActivate: [RoleGuard],
        data: { expectedRoles: 'ROLE_CLIENT' },
      },
      { path: 'support', component: ChatComponent, canActivate: [AuthGuard] },
      { path: 'fav-routes', component: FavRoutesComponent, canActivate: [RoleGuard], data: { expectedRoles: "ROLE_CLIENT" } },
      { path: 'active-rides', component: ActiveRidesComponent, canActivate: [RoleGuard], data: {expectedRoles: "ROLE_CLIENT"}},
      {
        path: 'driver',
        component: DriverHomepageComponent,
        canActivate: [RoleGuard],
        data: { expectedRoles: 'ROLE_DRIVER' },
        children: [
          { path: '', component: ActiveRidesComponent },
          { path: 'active-rides', component: ActiveRidesComponent },
        ],
      },
      { path: 'mytrips', component: RideHistoryComponent },
      {
        path: 'admin-support',
        component: AdminSupportComponent,
        canActivate: [RoleGuard],
        data: { expectedRoles: 'ROLE_ADMIN' },
      },
      { 
        path: 'reports', 
        component: ReportsComponent
      }
    ],
  },
  {
    path: 'admin',
    component: PrivateComponent,
    children: [
      {
        path: 'allUsers',
        component: AllUsersComponent
      },
      {
        path: 'addDriver',
        component: AdminAddDriverComponent
      },
      {
        path: 'notifications',
        component: AdminNotificationsComponent
      },
      {
        path: 'support',
        component: AdminSupportComponent,
        canActivate: [RoleGuard], data: { expectedRoles: "ROLE_ADMIN" },
      }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PrivateRoutingModule {}
