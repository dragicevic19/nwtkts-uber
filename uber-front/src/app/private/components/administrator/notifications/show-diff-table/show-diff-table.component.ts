import { Component, Input } from '@angular/core';
import { User } from 'src/app/private/models/User';
import { Notification } from 'src/app/private/models/Notification';

@Component({
  selector: 'app-show-diff-table',
  templateUrl: './show-diff-table.component.html',
  styleUrls: ['./show-diff-table.component.scss']
})
export class ShowDiffTableComponent {

  @Input() chosen!: User;
}
