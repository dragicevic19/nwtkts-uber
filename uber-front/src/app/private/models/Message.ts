import { User } from "./User";

export interface Message {
    sender: User;
    reciever: User;
    text: string;
    dateTime: Date;
}