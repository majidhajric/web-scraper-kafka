import {Component, OnInit} from '@angular/core';
import {UserSessionService} from '@app/services/user-session.service';
import {Observable} from 'rxjs';
import {UserInfo} from 'angular-oauth2-oidc';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html',
  styleUrls: ['./nav.component.scss']
})
export class NavComponent implements OnInit {

  constructor() {
  }

  ngOnInit(): void {
  }

}
