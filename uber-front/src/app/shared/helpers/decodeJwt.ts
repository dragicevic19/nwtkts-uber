
export default class DecodeJwt {
  static getUserFromAuthToken(): UserFromJwt | undefined {
    const auth_token = localStorage.getItem('access_token');
    if (auth_token) {
      let parsed = JSON.parse(atob(auth_token.split('.')[1]));
      let user = parsed as UserFromJwt;
      user.email = parsed.sub;
      return user;
    }
    else return undefined;
  }
}

export interface UserFromJwt {
  email: string;
  firstName: string;
  lastName: string;
  id: string;
  role: string;
  fullRegDone: boolean;
}
