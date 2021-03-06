import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserSessionService} from '@app/services/user-session.service';
import {Observable, Subscription} from 'rxjs';
import {UserInfo} from 'angular-oauth2-oidc';

@Component({
  selector: 'app-authentication-actions',
  templateUrl: './authentication-actions.component.html',
  styleUrls: ['./authentication-actions.component.scss']
})
export class AuthenticationActionsComponent implements OnInit, OnDestroy {

  userInfo: Observable<UserInfo>;
  userInfoSubscription: Subscription;
  constructor(private authService: UserSessionService) { }

  ngOnInit(): void {
    this.userInfo = this.authService.getUserInfo();
    this.userInfoSubscription = this.userInfo.subscribe();
  }

  ngOnDestroy(): void {
    this.userInfoSubscription.unsubscribe();
  }

  signIn() {
    this.authService.showLogInPage();
  }

  signOut() {
    this.authService.logOut();
  }
}
