export default class DecodeJwt {
  static getUserFromAuthToken(): UserFromJwt | undefined {
    const auth_token = localStorage.getItem('access_token');
    if (auth_token)
      return JSON.parse(atob(auth_token.split('.')[1])) as UserFromJwt;
    else return undefined;
  }
}

export interface UserFromJwt {
  firstName: string;
  lastName: string;
  id: string;
  role: string;
}
