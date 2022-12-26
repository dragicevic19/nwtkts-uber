export class SignInInfoDTO {

    firstName: string;
    lastName: string;
    email: string;
    password: string;
    repPassword: string;

    constructor(user: SignInFormData) {
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.email = user.email;
        this.password = user.password;
        this.repPassword = user.repPassword;
    }
}


export interface SignInFormData {
    email: string;
    firstName: string;
    lastName: string;
    password: string;
    repPassword: string;
}
