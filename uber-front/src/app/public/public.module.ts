import { NgModule } from '@angular/core';
import { PublicRoutingModule } from './public-routing.module';
import { PublicComponent } from './public.component';
import { SharedModule } from '../shared/shared.module';
import { ReactiveFormsModule } from '@angular/forms';
import { AdditionalSignupInfoComponent } from './pages/auth/additional-signup-info/additional-signup-info.component';
import { ForgotPassComponent } from './pages/auth/forgot-pass/forgot-pass.component';
import { LoginComponent } from './pages/auth/login/login.component';
import { ResetPassComponent } from './pages/auth/reset-pass/reset-pass.component';
import { SignupComponent } from './pages/auth/signup/signup.component';
import { MainpageComponent } from './pages/home/mainpage/mainpage.component';
import { CommonModule } from '@angular/common';


@NgModule({
  declarations: [
    PublicComponent,
    LoginComponent,
    SignupComponent,
    AdditionalSignupInfoComponent,
    ForgotPassComponent,
    ResetPassComponent,
    MainpageComponent
  ],
  imports: [
    CommonModule,
    PublicRoutingModule,
    ReactiveFormsModule,
    SharedModule,
  ]
})
export class PublicModule { }
