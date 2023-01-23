import { User } from "./User";

export interface Message {
    sender: User;
    receiver: User;
    text: string;
    dateTime: Date;
}