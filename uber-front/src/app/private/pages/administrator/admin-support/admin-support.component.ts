import { Component } from '@angular/core';

@Component({
  selector: 'app-admin-support',
  templateUrl: './admin-support.component.html',
  styleUrls: ['./admin-support.component.scss']
})
export class AdminSupportComponent {

  selectedChat: number | undefined = undefined;

  selectedChatEvent(id: number) {
    this.selectedChat = id;
  }
}
