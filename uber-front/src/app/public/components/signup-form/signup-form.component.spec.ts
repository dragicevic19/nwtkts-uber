import { ComponentFixture, fakeAsync, TestBed } from '@angular/core/testing';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/core/services/auth/auth.service';
import { of } from 'rxjs';

import { SignupFormComponent } from './signup-form.component';

describe('SignupFormComponent', () => {
  let component: SignupFormComponent;
  let fixture: ComponentFixture<SignupFormComponent>;
  let router: Router;
  let toastr: ToastrService;

  beforeEach(async () => {
    const routerMocked = jasmine.createSpyObj('router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [
        ReactiveFormsModule,
        FormsModule,
        CommonModule,
        MatFormFieldModule,
        MatInputModule,
        MatProgressSpinnerModule,
        BrowserAnimationsModule
      ],
      declarations: [SignupFormComponent],
      providers: [
        {
          provide: AuthService,
          useValue: {
            signUp: () => of({})
          }
        }, {
          provide: Router,
          useValue: routerMocked,
        }, {
          provide: ToastrService,
          useValue: {
            success: jasmine.createSpy("success"),
            error: jasmine.createSpy("error"),
          },
        }
      ]
    }).compileComponents();
  });
  beforeEach(() => {

    fixture = TestBed.createComponent(SignupFormComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    toastr = TestBed.inject(ToastrService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


  it('should be shown', () => {
    expect(component.signupForm).toBeDefined();
    expect(component.signupForm.invalid).toBeTruthy();
  });

  it("should hide password when called hideShowPass method", () => {
    component.hideShowPass();
    expect(component.isText).toBeTruthy();
    expect(component.eyeIcon).toBe("fa-eye");
    expect(component.type).toBe("text");
  });

  it("should return false, empty form", () => {
    component.signupForm.controls["email"].setValue("");
    component.signupForm.controls["firstName"].setValue("");
    component.signupForm.controls["lastName"].setValue("");
    component.signupForm.controls["password"].setValue("");
    component.signupForm.controls["repPassword"].setValue("");

    expect(component.signupForm.valid).toBeFalsy();
  });

  it("should return false, email invalid", () => {
    component.signupForm.controls["email"].setValue("testtest.com");
    component.signupForm.controls["firstName"].setValue("Test");
    component.signupForm.controls["lastName"].setValue("Test");
    component.signupForm.controls["password"].setValue("Test1234");
    component.signupForm.controls["repPassword"].setValue("Test1234");

    expect(component.signupForm.valid).toBeFalsy();
  });

  it("should return false, passwords are not matching", () => {
    component.signupForm.controls["email"].setValue("test@test.com");
    component.signupForm.controls["firstName"].setValue("Test");
    component.signupForm.controls["lastName"].setValue("Test");
    component.signupForm.controls["password"].setValue("Test12345");
    component.signupForm.controls["repPassword"].setValue("Test1234");

    expect(component.signupForm.valid).toBeFalsy();
  });

  it("should return false, not enough characters for password", () => {
    component.signupForm.controls["email"].setValue("test@test.com");
    component.signupForm.controls["firstName"].setValue("Test");
    component.signupForm.controls["lastName"].setValue("Test");
    component.signupForm.controls["password"].setValue("Test");
    component.signupForm.controls["repPassword"].setValue("Test");

    expect(component.signupForm.valid).toBeFalsy();
  });

  it("should return true, form is valid", () => {
    component.signupForm.controls["email"].setValue("test@test.com");
    component.signupForm.controls["firstName"].setValue("Test");
    component.signupForm.controls["lastName"].setValue("Test");
    component.signupForm.controls["password"].setValue("Test1234");
    component.signupForm.controls["repPassword"].setValue("Test1234");

    expect(component.signupForm.valid).toBeTruthy();
  });

  it('should not register, email is being used', () => {
    component.ngOnInit();
    component.signupForm.controls["email"].setValue("user@gmail.com");
    component.signupForm.controls["firstName"].setValue("Test");
    component.signupForm.controls["lastName"].setValue("Test");
    component.signupForm.controls["password"].setValue("Test1234");
    component.signupForm.controls["repPassword"].setValue("Test1234");
    component.onSingup();
    expect(component.confirmationStatus).toBeFalsy();
  });

  it('should register, all fields are correct', fakeAsync(() => {
    component.ngOnInit();
    component.signupForm.controls["email"].setValue("test@test.com");
    component.signupForm.controls["firstName"].setValue("Test");
    component.signupForm.controls["lastName"].setValue("Test");
    component.signupForm.controls["password"].setValue("Test1234");
    component.signupForm.controls["repPassword"].setValue("Test1234");
    component.onSingup();
    expect(toastr.success).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  }));
});
