import { Component, Input } from '@angular/core';
import { User } from 'src/app/private/models/User';
import {
  Storage,
  ref,
  uploadBytesResumable,
  getDownloadURL,
} from '@angular/fire/storage';
import { UserService } from 'src/app/core/services/user/user.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-profile-card-w-img',
  templateUrl: './profile-card-w-img.component.html',
  styleUrls: ['./profile-card-w-img.component.scss'],
})
export class ProfileCardWImgComponent {
  @Input() user!: User;

  constructor(
    private storage: Storage,
    private userService: UserService,
    private toastr: ToastrService
  ) {}


  changeImg() {
    document.getElementById('upload-file')?.click();
  }

  imgSelected(event: any) {
    const picture = event.target.files[0];
    const storageRef = ref(this.storage, picture.name);
    const uploadTask = uploadBytesResumable(storageRef, picture);
    uploadTask.on(
      'state_changed',
      (snapshot) => {
        const progress =
          (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
        if (progress === 0) {
          this.toastr.info('Picture is being uploaded');
        }
      },
      (error) => {
        console.log(error.message);
      },
      () => {
        getDownloadURL(uploadTask.snapshot.ref).then((downloadURL) => {
          this.setImageForUser(downloadURL);
        });
      }
    );
  }

  setImageForUser(downloadURL: string) {
    this.userService.changeProfilePicture(downloadURL).subscribe({
      next: () => {
        this.toastr.success('Successfully changed profile picture');
        this.user.image = downloadURL;
      },
      error: () => {
        this.toastr.error('An unexpected error occurred');
      },
    });
  }

  getDriverActivity() {
    if (this.user.driverActive) {return 'Active'} else {return 'Inactive'}
  }
}
