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
import { AdditionalSignupInfoFormComponent } from './components/additional-signup-info-form/additional-signup-info-form.component';
import { ForgotPassFormComponent } from './components/forgot-pass-form/forgot-pass-form.component';
import { LoginFormComponent } from './components/login-form/login-form.component';
import { SignupFormComponent } from './components/signup-form/signup-form.component';
import { ResetPassFormComponent } from './components/reset-pass-form/reset-pass-form.component';


@NgModule({
  declarations: [
    PublicComponent,
    LoginComponent,
    SignupComponent,
    AdditionalSignupInfoComponent,
    ForgotPassComponent,
    ResetPassComponent,
    MainpageComponent,
    AdditionalSignupInfoFormComponent,
    ForgotPassFormComponent,
    LoginFormComponent,
    SignupFormComponent,
    ResetPassFormComponent
  ],
  imports: [
    CommonModule,
    PublicRoutingModule,
    ReactiveFormsModule,
    SharedModule,
  ]
})
export class PublicModule { }
