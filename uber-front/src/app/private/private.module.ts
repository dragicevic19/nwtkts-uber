import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PrivateRoutingModule } from './private-routing.module';
import { PrivateComponent } from './private.component';
import { SharedModule } from '../shared/shared.module';
import { HomepageComponent } from './pages/homepage/homepage.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { ProfileCardWImgComponent } from './components/profile/profile-card-w-img/profile-card-w-img.component';
import { UserInfoComponent } from './components/profile/user-info/user-info.component';
import { PaymentMethodComponent } from './components/profile/payment-method/payment-method.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ChangePassComponent } from './components/profile/change-pass/change-pass.component';
import { AdministratorComponent } from './pages/administrator/administrator/administrator.component';
import { AdministratorNavbarComponent } from './components/administrator/administrator-navbar/administrator-navbar/administrator-navbar.component';
import { AllUsersComponent } from './components/administrator/all-users/all-users/all-users.component';
import { MyProfileComponent } from './components/administrator/my-profile/my-profile/my-profile.component';
import { AdminNotificationsComponent } from './components/administrator/admin-notifications/admin-notifications.component';


@NgModule({
  declarations: [
    PrivateComponent,
    HomepageComponent,
    ProfileComponent,
    ProfileCardWImgComponent,
    UserInfoComponent,
    PaymentMethodComponent,
    ChangePassComponent,
    AdministratorComponent,
    AdministratorNavbarComponent,
    AllUsersComponent,
    MyProfileComponent,
    AdminNotificationsComponent
  ],
  imports: [
    CommonModule,
    PrivateRoutingModule,
    SharedModule,
    ReactiveFormsModule,
    FormsModule
  ]
})
export class PrivateModule { }
