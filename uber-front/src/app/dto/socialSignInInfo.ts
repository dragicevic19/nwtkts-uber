export class SocialSignInInfoDTO {

    firstName: string = '';
    lastName: string = '';
    email: string = '';
    picture: string = '';
    auth: string = '';
    jwt: string = '';

    constructor(jwt: string, decodedJwt?: DecodedGoogleJwt, facebookInfo?: FacebookInfo) {
        
        if (decodedJwt) {
            this.firstName = decodedJwt.given_name;
            this.lastName = decodedJwt.family_name;
            this.email = decodedJwt.email;
            this.picture = decodedJwt.picture;
            this.auth = 'google';
            this.jwt = jwt;
        }
        else if (facebookInfo) {
            this.firstName = facebookInfo.firstName;
            this.lastName = facebookInfo.lastName;
            this.email = facebookInfo.email;
            this.picture = facebookInfo.photoUrl;
            this.auth = 'facebook';
            this.jwt = jwt;
        }
        else {
            throw new Error();
        }
    }
}

export interface DecodedGoogleJwt {
    email: string;
    family_name: string;
    given_name: string;
    name: string;
    picture: string;
}

export interface FacebookInfo {
    email: string;
    firstName: string;
    lastName: string;
    photoUrl: string;
}