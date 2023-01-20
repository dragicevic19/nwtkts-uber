import { Component } from '@angular/core';
import { Notification } from 'src/app/private/models/Notification';
import { User } from 'src/app/private/models/User';

@Component({
  selector: 'app-admin-notifications',
  templateUrl: './admin-notifications.component.html',
  styleUrls: ['./admin-notifications.component.scss'],
})
export class AdminNotificationsComponent {
  notifications: Notification[] = [
    {
      id: 1,
      oldInfo: {
        id: 1,
        image: 'https://firebasestorage.googleapis.com/v0/b/uber-123210.appspot.com/o/1charles.jpg?alt=media&token=c0299456-0554-4f96-b0f0-a84cd3361f56',
        firstName: 'John',
        lastName: 'Doe',
        email: 'johndoe@example.com',
        phone: '+1 555 555 5555',
        country: 'United States',
        city: 'New York',
        street: '123 Main St',
        role: 'ROLE_CLIENT',
        hasPassword: true,
        driverActive: false,
      },
      newInfo: {
        id: 1,
        image: 'https://via.placeholder.com/150',
        firstName: 'Jane',
        lastName: 'Doe',
        email: 'janedoe@example.com',
        phone: '+1 555 555 5555',
        country: 'United States',
        city: 'New York',
        street: '123 Main St',
        role: 'ROLE_ADMIN',
        hasPassword: true,
        driverActive: false,
      },
      status: 'pending',
    },
    {
      id: 2,
      oldInfo: {
        id: 2,
        image: 'https://via.placeholder.com/150',
        firstName: 'Bob',
        lastName: 'Smith',
        email: 'bobsmith@example.com',
        phone: '+1 555 555 5555',
        country: 'United States',
        city: 'Chicago',
        street: '456 Park Ave',
        role: 'ROLE_DRIVER',
        hasPassword: false,
        driverActive: false,

      },
      newInfo: {
        id: 2,
        image: 'https://firebasestorage.googleapis.com/v0/b/uber-123210.appspot.com/o/1charles.jpg?alt=media&token=c0299456-0554-4f96-b0f0-a84cd3361f56',
        firstName: 'Bob',
        lastName: 'Smith',
        email: 'bobsmith@example.com',
        phone: '+1 555 555 5555',
        country: 'United States',
        city: 'Los Angeles',
        street: '456 Park Ave',
        role: 'ROLE_DRIVER',
        hasPassword: true,
        driverActive: false,

      },
      status: 'pending',
    },
    {
      id: 3,
      oldInfo: {
        id: 1,
        image: 'https://firebasestorage.googleapis.com/v0/b/uber-123210.appspot.com/o/1charles.jpg?alt=media&token=c0299456-0554-4f96-b0f0-a84cd3361f56',
        firstName: 'John',
        lastName: 'Doe',
        email: 'johndoe@example.com',
        phone: '+1 555 555 5555',
        country: 'United States',
        city: 'New York',
        street: '123 Main St',
        role: 'ROLE_CLIENT',
        hasPassword: true,
        driverActive: false,

      },
      newInfo: {
        id: 1,
        image: 'https://via.placeholder.com/150',
        firstName: 'Jane',
        lastName: 'Doe',
        email: 'janedoe@example.com',
        phone: '+1 555 555 5555',
        country: 'United States',
        city: 'New York',
        street: '123 Main St',
        role: 'ROLE_ADMIN',
        hasPassword: true,
        driverActive: false,

      },
      status: 'accepted',
    },
    {
      id: 4,
      oldInfo: {
        id: 1,
        image: 'https://firebasestorage.googleapis.com/v0/b/uber-123210.appspot.com/o/1charles.jpg?alt=media&token=c0299456-0554-4f96-b0f0-a84cd3361f56',
        firstName: 'John',
        lastName: 'Doe',
        email: 'johndoe@example.com',
        phone: '+1 555 555 5555',
        country: 'United States',
        city: 'New York',
        street: '123 Main St',
        role: 'ROLE_CLIENT',
        hasPassword: true,
        driverActive: false,

      },
      newInfo: {
        id: 1,
        image: 'https://via.placeholder.com/150',
        firstName: 'Jane',
        lastName: 'Doe',
        email: 'janedoe@example.com',
        phone: '+1 555 555 5555',
        country: 'United States',
        city: 'New York',
        street: '123 Main St',
        role: 'ROLE_ADMIN',
        hasPassword: true,
        driverActive: false,

      },
      status: 'declined',
    },
  ];
  public chosen!: Notification;

  public onOpenModal(notification: Notification, mode: string): void {
    const container = document.getElementById('container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    if (mode === 'show-diff') {
      this.chosen = notification;
      button.setAttribute('data-target', '#show-diff-modal');
    }

    container?.appendChild(button);
    button.click();
  }

  public acceptChanges() {
    // Potrebna logika za odbijanje promena
    this.notifications.forEach(notification => {
      if (notification.id === this.chosen.id) {
        notification.status = 'accepted';
      }
    });
    let button = document.getElementById('close_button');
    button?.click();
  }

  public declineChanges() {
    // Potrebna logika za odbijanje promena
    this.notifications.forEach(notification => {
      if (notification.id === this.chosen.id) {
        notification.status = 'declined';
      }
    });
    let button = document.getElementById('close_button');
    button?.click();
  }

}
