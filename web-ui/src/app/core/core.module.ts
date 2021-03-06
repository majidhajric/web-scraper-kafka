import {ErrorHandler, NgModule, Optional, SkipSelf} from '@angular/core';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {OAuthModule, OAuthResourceServerErrorHandler} from 'angular-oauth2-oidc';
import {environment} from '../../environments/environment';


@NgModule({
  declarations: [],
  imports: [
    HttpClientModule,
    OAuthModule.forRoot({
      resourceServer: {
        allowedUrls: [environment.apiServer],
        sendAccessToken: true
      }
    })
  ],
  providers: [],
})
export class CoreModule {
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    // throwIfAlreadyLoaded(parentModule, 'CoreModule');
  }
}
