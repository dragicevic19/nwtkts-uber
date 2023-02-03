import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MdbModalRef } from 'mdb-angular-ui-kit/modal';
import { ToastrService } from 'ngx-toastr';
import { ClientService } from 'src/app/core/services/client/client.service';
import ValidateForm from 'src/app/shared/helpers/validateform';
import { RideRequest } from 'src/app/shared/models/RideRequest';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss']
})
export class ModalComponent implements OnInit {

  friendForm!: FormGroup;
  rideRequest!: RideRequest;

  constructor(
    public modalRef: MdbModalRef<ModalComponent>,
    private fb: FormBuilder,
    private clientService: ClientService,
    private toastr: ToastrService,
  ) {  }

  ngOnInit(): void {
    this.friendForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
    });
  }

  onSubmit() {
    if (this.friendForm.valid) {
      if (this.rideRequest.addedFriends.length === 3) {
        this.toastr.warning("There can be maximum of 4 passengers");
        return;
      }
      this.clientService.findUserByEmail(this.friendForm.controls['email'].value).subscribe({
        next: (res) => {
          if (this.rideRequest.addedFriends.map(e => e.id).indexOf(res.id) > -1) {
            this.toastr.warning("This friend has already been added");
          }
          else {
            this.friendForm.reset();
            this.rideRequest.addedFriends.push(res);
            this.toastr.success("Friend added!")
          }
        },
        error: (err) => {
          if (err.status === 404)
            this.toastr.error("Client with this email doesn't exist!")
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
    const myIndex = this.rideRequest.addedFriends.map(e => e.id).indexOf(index);

    if (myIndex > -1) this.rideRequest.addedFriends.splice(myIndex, 1);
    else throw new Error("Can't remove added friends");
  }
}
