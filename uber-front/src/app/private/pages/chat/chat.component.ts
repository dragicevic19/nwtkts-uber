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
export class ChatComponent  {
  
}
