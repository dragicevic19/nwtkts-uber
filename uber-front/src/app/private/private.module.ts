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
import { DriverHomepageComponent } from './pages/driver/driver-homepage/driver-homepage.component';
import { DriverPanelComponent } from './components/driver/driver-panel/driver-panel.component';
import { NgxPayPalModule } from 'ngx-paypal';
import { BuyTokensComponent } from './components/client/buy-tokens/buy-tokens.component';
import { ClientTransactionHistoryComponent } from './components/client/client-transaction-history/client-transaction-history.component';
import { ClientsBalanceInfoComponent } from './components/client/clients-balance-info/clients-balance-info.component';
import { ActiveRidesTableComponent } from './components/driver/active-rides-table/active-rides-table.component';
import { ActiveRidesComponent } from './components/driver/active-rides/active-rides.component';
import { AcceptSplitFareComponent } from './pages/client/accept-split-fare/accept-split-fare.component';
import { ClientTokensComponent } from './pages/client/client-tokens/client-tokens.component';
import { SplitFareTableComponent } from './components/client/split-fare-table/split-fare-table.component';


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
    AdminNotificationsComponent,
    DriverHomepageComponent,
    ActiveRidesComponent,
    DriverPanelComponent,
    ActiveRidesTableComponent,
    ClientTokensComponent,
    BuyTokensComponent,
    ClientTransactionHistoryComponent,
    ClientsBalanceInfoComponent,
    AcceptSplitFareComponent,
    SplitFareTableComponent,
  ],
  imports: [
    CommonModule,
    PrivateRoutingModule,
    SharedModule,
    ReactiveFormsModule,
    FormsModule,
    NgxPayPalModule,
  ]
})
export class PrivateModule { }
