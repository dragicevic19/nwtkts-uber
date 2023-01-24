import { Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { ChatService } from 'src/app/core/services/chat/chat.service';
import DecodeJwt, { UserFromJwt } from 'src/app/shared/helpers/decodeJwt';
import { Message } from '../../models/Message';
import { SendMessageDTO } from '../../models/SendMessageDTO';

@Component({
  selector: 'app-chat-body',
  templateUrl: './chat-body.component.html',
  styleUrls: ['./chat-body.component.scss']
})
export class ChatBodyComponent implements OnInit, OnDestroy, OnChanges {
  @Input() chatWith?: number;

  messages: Message[] = [];
  loggedIn: UserFromJwt | undefined = undefined;
  subscriptions: Subscription[] = [];

  textInputValue: string = "";

  constructor(private chatService: ChatService, private toastr: ToastrService) { }
  
  
  ngOnChanges(changes: SimpleChanges): void {
    if (this.chatWith) {
      this.loadPrevMessages();
    }
  }

  ngOnInit(): void {
    this.loggedIn = DecodeJwt.getUserFromAuthToken();
    this.chatService.initializeWebSocketConnection();

    if (this.chatWith) {
      this.loadPrevMessages();
    }

    this.subscriptions.push(this.chatService.newMessage.subscribe((message: Message) => {
      const messagesWithId = (this.chatWith) ? this.chatWith : this.loggedIn?.id;
      if (message.sender.id === messagesWithId || message.receiver?.id === messagesWithId) {
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
        receiverId: (this.chatWith) ? this.chatWith : null,
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