import { Component, EventEmitter, Input, Output } from '@angular/core';
import { User } from 'src/app/private/models/User';

@Component({
  selector: 'app-friend-ride-list',
  templateUrl: './friend-ride-list.component.html',
  styleUrls: ['./friend-ride-list.component.scss']
})
export class FriendRideListComponent {


  @Input() friendList?: User[]
  @Input() price?: number | null;
  @Output() friendRemovedEvent = new EventEmitter<number>();

  removeFriend(id: number) {
    this.friendRemovedEvent.emit(id);
  }
}
