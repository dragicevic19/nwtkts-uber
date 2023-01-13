import {
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import { Route } from '../../models/Route';

@Component({
  selector: 'app-routes-list',
  templateUrl: './routes-list.component.html',
  styleUrls: ['./routes-list.component.scss'],
})
export class RoutesListComponent {
  @Input()
  selectedRouteIndex!: number;
  @Input()
  routesJSON!: Route[];

  @Output()
  routeClickedEvent = new EventEmitter<number>();

  routeSelected(index: number) {
    this.routeClickedEvent.emit(index);
  }
}
