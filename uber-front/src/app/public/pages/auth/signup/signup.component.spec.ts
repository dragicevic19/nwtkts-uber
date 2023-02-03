// import { ComponentFixture, fakeAsync, TestBed } from '@angular/core/testing';

// import { SignupComponent } from './signup.component';
// import { FormsModule, ReactiveFormsModule } from '@angular/forms';
// import { CommonModule } from '@angular/common';
// import { MatFormFieldModule } from '@angular/material/form-field';
// import { MatInputModule } from '@angular/material/input';
// import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
// import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
// import { ToastrService } from 'ngx-toastr';
// import { Router } from '@angular/router';
// import { AuthService } from 'src/app/core/services/auth/auth.service';
// import { of } from 'rxjs';

// describe('SignupComponent', () => {
//   let component: SignupComponent;
//   let fixture: ComponentFixture<SignupComponent>;
//   let router: Router;
//   let toastr: ToastrService;

//   beforeEach(async () => {

//     const routerMocked = jasmine.createSpyObj('router', ['navigate']);

//     TestBed.configureTestingModule({
//       imports: [
//         ReactiveFormsModule,
//         FormsModule,
//         CommonModule,
//         MatFormFieldModule,
//         MatInputModule,
//         MatProgressSpinnerModule,
//         BrowserAnimationsModule
//       ],
//       declarations: [SignupComponent],
//       providers: [
//         {
//           provide: AuthService,
//           useValue: {
//             signUp: () => of({})
//           }
//         }, {
//           provide: Router,
//           useValue: routerMocked,
//         }, {
//           provide: ToastrService,
//           useValue: {
//             success: jasmine.createSpy("success"),
//             error: jasmine.createSpy("error"),
//           },
//         }
//       ]
//     }).compileComponents();
//   });

//   beforeEach(() => {

//     fixture = TestBed.createComponent(SignupComponent);
//     component = fixture.componentInstance;
//     router = TestBed.inject(Router);
//     toastr = TestBed.inject(ToastrService);
//     fixture.detectChanges();
//   });

//   it("should create", () => {
//     expect(component).toBeTruthy();
//   });

// });
