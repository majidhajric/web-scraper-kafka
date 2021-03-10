import {Injectable, OnDestroy} from '@angular/core';
import {AuthConfig, NullValidationHandler, OAuthEvent, OAuthService, UserInfo} from 'angular-oauth2-oidc';
import {environment} from '../../../environments/environment';
import {Observable, Subject, Subscription} from 'rxjs';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class UserSessionService implements OnDestroy {


  constructor(private oauthService: OAuthService, private router: Router) {
    this.configure();
  }

  private userInfoSubject: Subject<UserInfo> = new Subject<UserInfo>();
  private authEventSubscription: Subscription;

  private readonly authConfig: AuthConfig = {
    issuer: environment.authServer,
    redirectUri: window.location.origin,
    clientId: environment.authClientId,
    scope: 'openid profile email offline_access links_manage',
    responseType: 'code',
    // at_hash is not present in JWT token
    disableAtHashCheck: true,
    showDebugInformation: environment.debug === true,
    requireHttps: environment.production === true
  };

  ngOnDestroy(): void {
    if (this.authEventSubscription) {
      this.authEventSubscription.unsubscribe();
    }
  }

  private configure() {
    this.oauthService.configure(this.authConfig);
    this.oauthService.tokenValidationHandler = new NullValidationHandler();
    this.authEventSubscription = this.oauthService.events.subscribe((e: OAuthEvent) => this.OAuthEventHandler(e));
    if (this.authConfig.issuer) { // Don't ask why :(
      this.oauthService.loadDiscoveryDocumentAndTryLogin()
        .catch((error) => {
          console.log(error.message);
        });
    }
  }

  private OAuthEventHandler(event: OAuthEvent) {
    switch (event.type) {
      case 'token_received':
        this.oauthService.loadUserProfile()
          .then(userInfo => this.userInfoSubject.next(userInfo));
        break;
      case 'user_profile_loaded':
         this.router.navigate(['/home']);
         break;
      case 'logout':
        this.userInfoSubject.next(null);
        break;
      default:
        break;
    }
  }

  public showLogInPage() {
    this.oauthService.initLoginFlow();
  }

  public logOut() {
    this.oauthService.logOut();
  }

  public getUserInfo(): Observable<UserInfo> {
    return this.userInfoSubject.asObservable();
  }
}
