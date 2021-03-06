import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import {NavComponent} from '@layout/nav/nav.component';
import {ContentLayoutComponent} from '@layout/content-layout/content-layout.component';
import {SharedModule} from '@shared/shared.module';
import {CoreModule} from '@app/core.module';
import { LandingComponent } from '@layout/landing/landing.component';
import { AuthenticationActionsComponent } from '@layout/authentication-actions/authentication-actions.component';

@NgModule({
  declarations: [
    AppComponent,
    NavComponent,
    ContentLayoutComponent,
    LandingComponent,
    AuthenticationActionsComponent,
  ],
  imports: [
    BrowserModule.withServerTransition({ appId: 'serverApp' }),
    AppRoutingModule,
    BrowserAnimationsModule,
    RouterModule,
    // core & shared
    CoreModule,
    SharedModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
