export class SocialSignInInfoDTO {
  firstName: string = '';
  lastName: string = '';
  email: string = '';
  picture: string = '';

  constructor(facebookInfo: FacebookInfo) {
    this.firstName = facebookInfo.firstName;
    this.lastName = facebookInfo.lastName;
    this.email = facebookInfo.email;
    this.picture = facebookInfo.photoUrl;
  }
}

export interface FacebookInfo {
  email: string;
  firstName: string;
  lastName: string;
  photoUrl: string;
}
