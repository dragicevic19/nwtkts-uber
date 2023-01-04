import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './components/navbar/navbar.component';
import { RouterModule } from '@angular/router';
import { NotFoundPageComponent } from './components/not-found-page/not-found-page.component';
import { MapComponent } from './components/map/map.component';
import { LeafletModule } from '@asymmetrik/ngx-leaflet';
import { ReqRideFormComponent } from './components/req-ride-form/req-ride-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import {  MatTooltipModule  } from '@angular/material/tooltip';
import { MatInputModule } from '@angular/material/input';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatFormFieldModule } from '@angular/material/form-field';
import { AutocompleteComponent } from './components/autocomplete/autocomplete.component';

@NgModule({
  declarations: [
    NavbarComponent,
    NotFoundPageComponent,
    MapComponent,
    ReqRideFormComponent,
    AutocompleteComponent
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
  ],
  exports: [
    NavbarComponent,
    MapComponent,
    ReqRideFormComponent,
  ]
})
export class SharedModule { }
