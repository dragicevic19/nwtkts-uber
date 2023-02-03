export class SocialSignInInfoDTO {
  firstName = '';
  lastName = '';
  email = '';
  picture = '';

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
