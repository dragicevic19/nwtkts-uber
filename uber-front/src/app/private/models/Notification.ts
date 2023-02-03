import { User } from "./User";
export interface Notification {
    id: number;
    oldInfo: User;
    newInfo: User;
    status: string;
}