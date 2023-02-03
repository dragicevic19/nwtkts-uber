import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ClientService } from 'src/app/core/services/client/client.service';
import { FavRoute } from 'src/app/shared/models/FavRoute';
import { ReqRideFromFavRouteComponent } from '../req-ride-from-fav-route/req-ride-from-fav-route.component';

@Component({
  selector: 'app-fav-routes-table',
  templateUrl: './fav-routes-table.component.html',
  styleUrls: ['./fav-routes-table.component.scss']
})
export class FavRoutesTableComponent implements OnInit {

  favRoutes: FavRoute[] = [];

  constructor(private clientService: ClientService,
    private modalService: NgbModal) { }

  ngOnInit(): void {
    this.clientService.getClientsFavRoutes().subscribe({
      next: (res: FavRoute[]) => {
        this.favRoutes = res;
      },
      error: (err) => {
        console.log(err);
      }
    });
  }

  onOrder(route: FavRoute) {
    const modalRef = this.modalService.open(ReqRideFromFavRouteComponent, { size: 'lg' });
    modalRef.componentInstance.favRoute = route;
  }

  onDelete(route: FavRoute) {
    this.clientService.removeFavRoute(route.id!).subscribe({
      next: (res) => {
        this.favRoutes = this.favRoutes.filter(x => x.id !== route.id);
      },
      error: (err) => {
        console.log(err);
      }
    });
  }
}
