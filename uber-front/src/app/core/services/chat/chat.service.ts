import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import { Message } from 'src/app/private/models/Message';
import { SendMessageDTO } from 'src/app/private/models/SendMessageDTO';
import { HttpClient } from '@angular/common/http';
import { ChatListItem } from 'src/app/private/models/ChatListItem';


@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private baseUrl = 'http://localhost:8080/chat/';


  private stompClient: any;
  newMessage: Subject<Message> = new Subject<Message>();

  constructor(private http: HttpClient) { }

  initializeWebSocketConnection() {
    if (this.stompClient) return;
    const ws = new SockJS('http://localhost:8080/socket');
    this.stompClient = Stomp.over(ws);
    this.stompClient.debug = null;
    const that = this;
    this.stompClient.connect({}, function () {
      that.openGlobalSocket();
    });
  }

  openGlobalSocket() {
    this.stompClient.subscribe('/map-updates/chat/new-message',
      (message: { body: string }) => {
        const mes: Message = JSON.parse(message.body);
        this.newMessage.next(mes);
      }
    );
  }

  sendMessage(message: SendMessageDTO): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}newMessage`, message);
  }

  loadPrevMessagesForUser(userId: number): Observable<Message[]> {
    return this.http.get<Message[]>(`${this.baseUrl}getAllMessagesForUser/${userId}`);
  }

  loadChatsForAdmin(): Observable<ChatListItem[]> {
    return this.http.get<ChatListItem[]>(`${this.baseUrl}getChatsForAdmin`);
  }

}
