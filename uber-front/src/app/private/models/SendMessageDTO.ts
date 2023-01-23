export interface SendMessageDTO {
    senderId: number;
    receiverId: number | null;
    text: string;
}