import { Component, OnInit } from '@angular/core';
import { ClientService } from 'src/app/core/services/client/client.service';
import { FavRoute } from 'src/app/shared/models/FavRoute';
import { Route } from 'src/app/shared/models/Route';

@Component({
  selector: 'app-fav-routes-table',
  templateUrl: './fav-routes-table.component.html',
  styleUrls: ['./fav-routes-table.component.scss']
})
export class FavRoutesTableComponent implements OnInit {

  favRoutes: FavRoute[] = [];

  constructor(private clientService: ClientService) { }

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
    throw new Error('Method not implemented.');
  }

  onDelete(route: FavRoute) {
    this.clientService.removeFavRoute(route.id!).subscribe({
      next: (res) => {
        this.favRoutes = this.favRoutes.filter(x => x.id !== route.id);
      },
      error: (res) => {
        console.log(res);
      }
    });
  }
}
