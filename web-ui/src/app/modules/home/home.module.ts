import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {HomeRoutingModule} from './home-routing.module';
import {HomeComponent} from './home/home.component';
import { LinkListComponent } from './home/link-list/link-list.component';
import { LinkViewComponent } from './home/link-view/link-view.component';
import {MatTableModule} from '@angular/material/table';
import {MaterialModule} from '../../shared/material.module';
import { NewLinkComponent } from './home/new-link/new-link.component';


@NgModule({
  declarations: [HomeComponent, LinkListComponent, LinkViewComponent, NewLinkComponent],
  imports: [
    CommonModule,
    HomeRoutingModule,
    MatTableModule,
    MaterialModule
  ]
})
export class HomeModule {
}
