import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { PrivateRoutingModule } from './private-routing.module';
import { PrivateComponent } from './private.component';
import { SharedModule } from '../shared/shared.module';
import { HomepageComponent } from './pages/client/homepage/homepage.component';
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

import { BuyTokensComponent } from './components/client/buy-tokens/buy-tokens.component';
import { ClientTransactionHistoryComponent } from './components/client/client-transaction-history/client-transaction-history.component';
import { ClientsBalanceInfoComponent } from './components/client/clients-balance-info/clients-balance-info.component';
import { AcceptSplitFareComponent } from './pages/client/accept-split-fare/accept-split-fare.component';
import { ClientTokensComponent } from './pages/client/client-tokens/client-tokens.component';
import { SplitFareTableComponent } from './components/client/split-fare-table/split-fare-table.component';


import { RideHistoryComponent } from './pages/ride-history/ride-history.component';

import { MatTableModule } from '@angular/material/table';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { HttpClientModule } from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { RideHistoryDetailedUserModalComponent } from './components/rideHistory/ride-history-detailed-user-modal/ride-history-detailed-user-modal.component';
import { MdbModalModule } from 'mdb-angular-ui-kit/modal';
import { RideHistoryDetailedDriverModalComponent } from './components/rideHistory/ride-history-detailed-driver-modal/ride-history-detailed-driver-modal.component';
import { RideHistoryDetailedAdminModalComponent } from './components/rideHistory/ride-history-detailed-admin-modal/ride-history-detailed-admin-modal.component';
import { StaticMapComponent } from './components/static-map/static-map.component';
import { ActiveRidesTableComponent } from './components/driver/active-rides-table/active-rides-table.component';
import { ActiveRidesComponent } from './components/driver/active-rides/active-rides.component';
import { DriverPanelComponent } from './components/driver/driver-panel/driver-panel.component';

import { NgxPayPalModule } from 'ngx-paypal';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { ChatComponent } from './pages/chat/chat.component';
import { AdminSupportComponent } from './pages/administrator/admin-support/admin-support.component';
import { ChatBodyComponent } from './components/chat-body/chat-body.component';
import { ChatListComponent } from './components/administrator/chat-list/chat-list.component';

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
    RideHistoryComponent,
    RideHistoryDetailedUserModalComponent,
    RideHistoryDetailedDriverModalComponent,
    RideHistoryDetailedAdminModalComponent,
    StaticMapComponent,
    ChatComponent,
    AdminSupportComponent,
    ChatBodyComponent,
    ChatListComponent,
  ],
  imports: [
    CommonModule,
    PrivateRoutingModule,
    SharedModule,
    ReactiveFormsModule,
    FormsModule,
    MatTableModule,
    MatSlideToggleModule,
    MatPaginatorModule,
    MatSortModule,
    MatProgressSpinnerModule,
    HttpClientModule,
    BrowserModule,
    MdbModalModule,
    NgxPayPalModule,
    LeafletModule
  ],
  exports: [
    StaticMapComponent
  ]
})
export class PrivateModule { }
