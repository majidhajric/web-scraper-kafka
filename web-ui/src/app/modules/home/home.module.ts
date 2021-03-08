import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {HomeRoutingModule} from './home-routing.module';
import {HomeComponent} from './view/home.component';
import { LinkListComponent } from './view/link-list/link-list.component';
import { LinkViewComponent } from './view/link-view/link-view.component';
import {MatTableModule} from '@angular/material/table';
import {MaterialModule} from '../../shared/material.module';
import { CreateLinkComponent } from './edit/create-link/create-link.component';
import {SharedModule} from '../../shared/shared.module';


@NgModule({
  declarations: [HomeComponent, LinkListComponent, LinkViewComponent, CreateLinkComponent],
  imports: [
    CommonModule,
    HomeRoutingModule,
    MatTableModule,
    MaterialModule,
    SharedModule
  ]
})
export class HomeModule {
}
