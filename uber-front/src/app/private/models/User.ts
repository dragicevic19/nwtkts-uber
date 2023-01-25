export interface User {
  id: number;
  image: string;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  country: string;
  city: string;
  street: string;
  role: string;
  hasPassword: boolean;
  driverActive: boolean;
  blocked: boolean;
}