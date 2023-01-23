import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginGuard } from '../core/guards/login/login.guard';
import { NotFoundPageComponent } from '../shared/components/not-found-page/not-found-page.component';
import { AdditionalSignupInfoComponent } from './pages/auth/additional-signup-info/additional-signup-info.component';
import { ForgotPassComponent } from './pages/auth/forgot-pass/forgot-pass.component';
import { LoginComponent } from './pages/auth/login/login.component';
import { ResetPassComponent } from './pages/auth/reset-pass/reset-pass.component';
import { SignupComponent } from './pages/auth/signup/signup.component';
import { MainpageComponent } from './pages/home/mainpage/mainpage.component';
import { PublicComponent } from './public.component';
import { AdministratorComponent } from '../private/pages/administrator/administrator/administrator.component';

const routes: Routes = [
  {
    path: '',
    component: PublicComponent,
    canActivate: [LoginGuard],
    children: [
      {
        path: '',
        component: MainpageComponent,
      }
    ]
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [LoginGuard]
  },
  {
    path: 'signup',
    component: SignupComponent,
    canActivate: [LoginGuard]
  },
  { 
    path: 'forgot',
    component: ForgotPassComponent,
    canActivate: [LoginGuard]
  },
  { 
    path: 'reset-pass',
    component: ResetPassComponent,
    canActivate: [LoginGuard]
  },
  {
    path: 'additionalInfo',
    component: AdditionalSignupInfoComponent,
  },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PublicRoutingModule { }
