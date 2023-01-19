import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../core/guards/auth/auth.guard';
import { RoleGuard } from '../core/guards/role/role.guard';
import { ActiveRidesComponent } from './pages/driver/driver-homepage/active-rides/active-rides/active-rides.component';
import { DriverHomepageComponent } from './pages/driver/driver-homepage/driver-homepage.component';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { PrivateComponent } from './private.component';

const routes: Routes = [
  {
    path: 'uber',
    component: PrivateComponent,
    canActivate: [AuthGuard],
    children: [
      {path: '', component: HomepageComponent, canActivate: [RoleGuard], data: {expectedRoles: "ROLE_CLIENT"}},
      {path: 'profile', component: ProfileComponent},
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
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PrivateRoutingModule { }
