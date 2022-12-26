export class AdditionalSignInInfoDTO {
  street: string;
  city: string;
  country: string;
  phoneNumber: string;

  constructor(user: AdditionalSignInFormData) {
    this.street = user.street;
    this.city = user.city;
    this.country = user.country;
    this.phoneNumber = user.phoneNumber;
  }
}

export interface AdditionalSignInFormData {
  street: string;
  city: string;
  country: string;
  phoneNumber: string;
}
