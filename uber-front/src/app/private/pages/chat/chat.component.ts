import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { ChatService } from 'src/app/core/services/chat/chat.service';
import DecodeJwt, { UserFromJwt } from 'src/app/shared/helpers/decodeJwt';
import { Message } from '../../models/Message';
import { SendMessageDTO } from '../../models/SendMessageDTO';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit, OnDestroy {
  @Input() chatWith?: number;

  messages: Message[] = [];
  loggedIn: UserFromJwt | undefined = undefined;
  subscriptions: Subscription[] = [];

  textInputValue: string = "";

  constructor(private chatService: ChatService, private toastr: ToastrService) { }

  ngOnInit(): void {
    this.loggedIn = DecodeJwt.getUserFromAuthToken();
    this.chatService.initializeWebSocketConnection();

    this.subscriptions.push(this.chatService.newMessage.subscribe((message: Message) => {
      const messagesWithId = (this.chatWith) ? this.chatWith : this.loggedIn?.id;
      if (message.sender.id === messagesWithId || message.reciever?.id === messagesWithId) {
        this.messages.push(message);
      }
    }));
  }

  ngOnDestroy(): void {
    for (let sub of this.subscriptions) {
      sub.unsubscribe();
    }
  }

  loadPrevMessages() {
    
    if (this.chatWith) {
      this.chatService.loadPrevMessagesForUser(this.chatWith).subscribe({
        next: (res: Message[]) => {
          this.messages = res;
        },
        error: (err) => {
          this.toastr.error(err.error);
        }
      });
    }
    else {
      this.chatService.loadPrevMessagesForUser(this.loggedIn!.id).subscribe({
        next: (res: Message[]) => {
          this.messages = res;
        },
        error: (err) => {
          this.toastr.error(err.error);
        }
      });
    }
  }

  sendMessage() {
    if (this.textInputValue) {
      let message: SendMessageDTO = {
        senderId: this.loggedIn!.id,
        recieverId: null,
        text: this.textInputValue,
      };
      this.chatService.sendMessage(message).subscribe({
        next: (res) => {
          this.textInputValue = '';
        },
        error: (err) => {
          this.toastr.error(err.error);
        }
      })
    }
  }
}
