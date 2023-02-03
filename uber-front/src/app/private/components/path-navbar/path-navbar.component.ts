import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-path-navbar',
  templateUrl: './path-navbar.component.html',
  styleUrls: ['./path-navbar.component.scss']
})
export class PathNavbarComponent {
  @Input() homePath = '';
  @Input() pageName = '';

}
