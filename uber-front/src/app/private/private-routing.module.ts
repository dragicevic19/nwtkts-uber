import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from '../core/guards/auth/auth.guard';
import { RoleGuard } from '../core/guards/role/role.guard';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { PrivateComponent } from './private.component';

const routes: Routes = [
  {
    path: 'uber',
    component: PrivateComponent,
    canActivate: [AuthGuard],
    children: [
      {path: '', component: HomepageComponent},
      {path: 'profile', component: ProfileComponent},
      // {path: 'ride', component: RideComponent, canActivate: [RoleGuard], data: {expectedRoles: "ROLE_CLIENT"}},
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PrivateRoutingModule { }
