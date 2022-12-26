export class LoginInfoDTO {
    email: string;
    password: string;

    constructor(formValue: RawFormValue) {
        this.email = formValue.email;
        this.password = formValue.password;
    }
}

export interface RawFormValue {
    email: string;
    password: string;
}