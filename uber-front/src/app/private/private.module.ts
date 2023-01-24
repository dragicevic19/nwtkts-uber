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
import { AllUsersComponent } from './components/administrator/all-users/all-users/all-users.component';
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
import { RideCancelationModalComponent } from './components/driver/ride-cancelation-modal/ride-cancelation-modal.component';
import { FavRoutesComponent } from './pages/client/fav-routes/fav-routes.component';
import { FavRoutesTableComponent } from './components/client/fav-routes-table/fav-routes-table.component';
import { ReqRideFromFavRouteComponent } from './components/client/req-ride-from-fav-route/req-ride-from-fav-route.component';
import { AdminAddDriverComponent } from './components/administrator/admin-add-driver/admin-add-driver.component';
import { EditModalComponent } from './components/administrator/all-users/edit-modal/edit-modal.component';
import { UserCardComponent } from './components/administrator/all-users/user-card/user-card.component';
import { VehicleDetailsComponent } from './components/administrator/admin-add-driver/vehicle-details/vehicle-details.component';
import { DriverDetailsComponent } from './components/administrator/admin-add-driver/driver-details/driver-details.component';
import { AdminNotificationsComponent } from './pages/administrator/admin-notifications/admin-notifications.component';
import { ShowDiffModalComponent } from './components/administrator/notifications/show-diff-modal/show-diff-modal.component';
import { ShowDiffTableComponent } from './components/administrator/notifications/show-diff-table/show-diff-table.component';
import { UserProfileComponent } from './pages/administrator/user-profile/user-profile.component';

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
    AllUsersComponent,
    AdminAddDriverComponent,
    EditModalComponent,
    UserCardComponent,
    VehicleDetailsComponent,
    DriverDetailsComponent,
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
    RideCancelationModalComponent,
    FavRoutesComponent,
    FavRoutesTableComponent,
    ReqRideFromFavRouteComponent,
    AdminAddDriverComponent,
    ShowDiffModalComponent,
    ShowDiffTableComponent,
    UserProfileComponent,
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
