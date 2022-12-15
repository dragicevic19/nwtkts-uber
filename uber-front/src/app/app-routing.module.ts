import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdditionalSignupInfoComponent } from './components/additional-signup-info/additional-signup-info.component';
import { ForgotPassComponent } from './components/forgot-pass/forgot-pass.component';
import { LoginComponent } from './components/login/login.component';
import { MainpageComponent } from './components/mainpage/mainpage.component';
import { ResetPassComponent } from './components/reset-pass/reset-pass.component';
import { SignupComponent } from './components/signup/signup.component';
import { AuthGuardService } from './services/auth-guard.service';

const routes: Routes = [
  { path: '', component: MainpageComponent },
  { path: 'login', component: LoginComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'forgot', component: ForgotPassComponent },
  { path: 'reset-pass', component: ResetPassComponent },
  // {path:'mainpage', component: MainpageComponent, canActivate: [AuthGuardService]},
  { path: 'mainpage', component: MainpageComponent },

  { path: 'additionalInfo', component: AdditionalSignupInfoComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
