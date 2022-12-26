import { Component, Input, OnInit } from '@angular/core';
import { User } from 'src/app/private/models/User';

@Component({
  selector: 'app-profile-card-w-img',
  templateUrl: './profile-card-w-img.component.html',
  styleUrls: ['./profile-card-w-img.component.scss']
})
export class ProfileCardWImgComponent implements OnInit{
  @Input() user!: User;

  constructor(){}

  ngOnInit(): void {}

}
