export interface SendMessageDTO {
    senderId: number;
    recieverId: number | null;
    text: string;
}