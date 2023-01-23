import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import { Subscription } from 'rxjs';
import { ChatService } from 'src/app/core/services/chat/chat.service';
import { ChatListItem } from 'src/app/private/models/ChatListItem';
import { Message } from 'src/app/private/models/Message';
import { RideHistoryComponent } from 'src/app/private/pages/ride-history/ride-history.component';
import DecodeJwt, { UserFromJwt } from 'src/app/shared/helpers/decodeJwt';

@Component({
  selector: 'app-chat-list',
  templateUrl: './chat-list.component.html',
  styleUrls: ['./chat-list.component.scss']
})
export class ChatListComponent implements OnInit, OnDestroy {
  @Output() selectedChatEvent = new EventEmitter<number>();

  chatList: ChatListItem[] = [];
  subscriptions: Subscription[] = [];
  loggedIn: UserFromJwt | undefined = undefined;

  constructor(private chatService: ChatService) { }

  ngOnInit(): void {
    this.loggedIn = DecodeJwt.getUserFromAuthToken();
    this.chatService.initializeWebSocketConnection();

    this.loadChats();

    this.subscriptions.push(this.chatService.newMessage.subscribe((message: Message) => {
      const self = this.loggedIn?.id;
      const other = (message.sender.id === self) ? message.receiver : message.sender;

      const foundedInChatList = this.chatList.find(x => x.id === other.id);
      if (foundedInChatList) {
        this.chatList = this.chatList.filter(x => x.id !== other.id);
        this.chatList.unshift(foundedInChatList); // stavljam ga samo na prvo mesto u listi
      }
      else {
        const newItem: ChatListItem = {
          id: other.id,
          name: other.firstName + ' ' + other.lastName,
          img: other.image
        };
        this.chatList.unshift(newItem);
      }
    }));
  }

  ngOnDestroy(): void {
    for (let sub of this.subscriptions) {
      sub.unsubscribe();
    }
  }

  selectedChat(id: number) {
    this.selectedChatEvent.emit(id);
  }

  loadChats() {
    this.chatService.loadChatsForAdmin().subscribe({
      next: (res: ChatListItem[]) => {
        this.chatList = res.reverse();
      },
      error: (err) => {
        console.log(err);
      }
    })
  }
}
