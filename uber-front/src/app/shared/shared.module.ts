import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './components/navbar/navbar.component';
import { RouterModule } from '@angular/router';
import { NotFoundPageComponent } from './components/not-found-page/not-found-page.component';
import { MapComponent } from './components/map/map.component';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { ReqRideFormComponent } from './components/order-ride/req-ride-form/req-ride-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import {  MatTooltipModule  } from '@angular/material/tooltip';
import { MatInputModule } from '@angular/material/input';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { GeoapifyGeocoderAutocompleteModule } from '@geoapify/angular-geocoder-autocomplete';
import { RoutesListComponent } from './components/order-ride/routes-list/routes-list.component';
import { ModalComponent } from './components/order-ride/add-friend-to-ride-modal/modal.component';
import { FriendRideListComponent } from './components/order-ride/friend-ride-list/friend-ride-list.component';
import { SelectVehicleListComponent } from './components/order-ride/select-vehicle-list/select-vehicle-list.component';

@NgModule({
  declarations: [
    NavbarComponent,
    NotFoundPageComponent,
    MapComponent,
    ReqRideFormComponent,
    RoutesListComponent,
    ModalComponent,
    FriendRideListComponent,
    SelectVehicleListComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    LeafletModule,
    ReactiveFormsModule,
    HttpClientModule,
    FormsModule,
    MatAutocompleteModule,
    MatTooltipModule,
    MatInputModule,
    MatFormFieldModule,
    GeoapifyGeocoderAutocompleteModule.withConfig('b293cff4266e42dc8e383de20069faf9'),
  ],
  exports: [
    NavbarComponent,
    MapComponent,
    ReqRideFormComponent,
  ]
})
export class SharedModule { }
