import { Component, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ShowDiffModalComponent } from 'src/app/private/components/administrator/notifications/show-diff-modal/show-diff-modal.component';
import { Notification } from 'src/app/private/models/Notification';
import { AdministratorService } from 'src/app/core/services/administrator/administrator.service';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
@Component({
  selector: 'app-admin-notifications',
  templateUrl: './admin-notifications.component.html',
  styleUrls: ['./admin-notifications.component.scss']
})
export class AdminNotificationsComponent {

  constructor(
    private modalService: NgbModal, 
    private administratorService: AdministratorService,
    private router: Router,
    ) {}

  showDiff(notification: Notification) {
      const modalRef = this.modalService.open(ShowDiffModalComponent,  { size: 'lg' });
      modalRef.componentInstance.chosen = notification;
      modalRef.closed.subscribe((promenljiva: string) => {
        // console.log(promenljiva);
      })
    }

  notifications!: Notification[];
  chosen!: Notification;

  ngOnInit() {
    this.getNotifications();
  }

  public getNotifications() {
    this.administratorService.getNotifications().subscribe(
      (response: Notification[]) => {
        this.notifications = response;
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public searchNotifications(key: string): void {
    const results: Notification[] = [];
    for (const notifications of this.notifications) {
      if (notifications.oldInfo.firstName.toLowerCase().indexOf(key.toLowerCase()) !== -1
      || notifications.oldInfo.lastName.toLowerCase().indexOf(key.toLowerCase()) !== -1
      || notifications.oldInfo.email.toLowerCase().indexOf(key.toLowerCase()) !== -1
      || notifications.status.toLowerCase().indexOf(key.toLowerCase()) !== -1
      || notifications.oldInfo.role.toLowerCase().indexOf(key.toLowerCase()) !== -1) {
        results.push(notifications);
      }
    }
    this.notifications = results;
    if (results.length === 0 || !key) {
      this.getNotifications();
    }
  }

  getRole(notification: Notification) {
    if (notification.oldInfo.role === 'ROLE_ADMIN') {
      return 'Admin';
    }
    else if (notification.oldInfo.role === 'ROLE_CLIENT') {
      return 'Client';
    }
    else if (notification.oldInfo.role === 'ROLE_DRIVER') {
      return 'Driver';
    }
    else {
      return 'Error';
    }
  }

  countPendingNotifications(): number {
    let i = 0;
    for (let notification of this.notifications) {
      if (notification.status === 'pending') {
        i += 1;
      }
    }
    return i;
  }

  redirectToProfile(id: number) {
    console.log('ovde sam');
    this.router.navigate([`uber/profile/${id}`])
  }


}
