import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MdbModalRef } from 'mdb-angular-ui-kit/modal';
import { ToastrService } from 'ngx-toastr';
import { UserService } from 'src/app/core/services/user/user.service';
import { User } from 'src/app/private/models/User';
import ValidateForm from '../../helpers/validateform';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss']
})
export class ModalComponent implements OnInit {


  friendForm!: FormGroup;

  addedFriends: User[] = [];

  price: number | null = null;
  pricePerPerson: any = null;

  constructor(
    public modalRef: MdbModalRef<ModalComponent>,
    private fb: FormBuilder,
    private userService: UserService,
    private toastr: ToastrService,
  ) { }

  ngOnInit(): void {
    if (this.price && this.pricePerPerson)
      this.pricePerPerson.price = Number((this.price / (this.addedFriends.length + 1)).toFixed(2));

    this.friendForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
    });
  }

  onSubmit() {
    if (this.friendForm.valid) {
      this.userService.findUserByEmail(this.friendForm.controls['email'].value).subscribe({
        next: (res) => {
          if (this.addedFriends.map(e => e.id).indexOf(res.id) > -1)
          {
            this.toastr.warning("This friend has already been added");
          }
          else {
            this.friendForm.reset();
            this.addedFriends.push(res);
            if (this.price)
              this.pricePerPerson.price = Number((this.price / (this.addedFriends.length + 1)).toFixed(2));
            this.toastr.success("Friend added!")
          }
        },
        error: (err) => {
          if (err.status === 404)
            this.toastr.error("User with this email doesn't exist!")
          else if (err.status === 400)
            this.toastr.error("That's your email address!");
        }
      })
    }
    else {
      ValidateForm.validateAllFormFields(this.friendForm);
    }    
  }

  removeFriend(index: number) {
    const myIndex = this.addedFriends.map(e => e.id).indexOf(index);

    if (myIndex > -1) this.addedFriends.splice(myIndex, 1);
    else throw new Error("Can't remove added friends");

    if (this.price)
      this.pricePerPerson.price = Number((this.price / (this.addedFriends.length + 1)).toFixed(2));
  }
}
