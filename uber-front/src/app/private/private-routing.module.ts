import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../core/guards/auth/auth.guard';
import { RoleGuard } from '../core/guards/role/role.guard';
import { ActiveRidesComponent } from './components/driver/active-rides/active-rides.component';
import { AcceptSplitFareComponent } from './pages/client/accept-split-fare/accept-split-fare.component';
import { ClientTokensComponent } from './pages/client/client-tokens/client-tokens.component';
import { DriverHomepageComponent } from './pages/driver/driver-homepage/driver-homepage.component';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { PrivateComponent } from './private.component';
import { RideHistoryComponent } from './pages/ride-history/ride-history.component';


const routes: Routes = [
  {
    path: 'uber',
    component: PrivateComponent,
    canActivate: [AuthGuard],
    children: [
      {path: '', component: HomepageComponent, canActivate: [RoleGuard], data: {expectedRoles: "ROLE_CLIENT"}},
      {path: 'profile', component: ProfileComponent},
      {path: 'wallet', component: ClientTokensComponent, canActivate: [RoleGuard], data: {expectedRoles: "ROLE_CLIENT"}},
      {path: 'split-fare', component: AcceptSplitFareComponent, canActivate: [RoleGuard], data: {expectedRoles: "ROLE_CLIENT"}},
      
      {
        path: 'driver',
        component: DriverHomepageComponent, 
        canActivate: [RoleGuard], 
        data: {expectedRoles: "ROLE_DRIVER"},
        children: [
          {path: '', component: ActiveRidesComponent},
          {path: 'active-rides', component: ActiveRidesComponent}
        ]
      },
      // {path: 'ride', component: RideComponent, canActivate: [RoleGuard], data: {expectedRoles: "ROLE_CLIENT"}},
      {path: 'mytrips', component: RideHistoryComponent }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PrivateRoutingModule { }
