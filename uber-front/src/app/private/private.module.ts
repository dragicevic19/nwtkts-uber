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


@NgModule({
  declarations: [
    PrivateComponent,
    HomepageComponent,
    ProfileComponent,
    ProfileCardWImgComponent,
    UserInfoComponent,
    PaymentMethodComponent
  ],
  imports: [
    CommonModule,
    PrivateRoutingModule,
    SharedModule
  ]
})
export class PrivateModule { }
