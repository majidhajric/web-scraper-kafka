import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {HomeRoutingModule} from './home-routing.module';
import {HomeComponent} from './view/home.component';
import { LinkListComponent } from './view/link-list/link-list.component';
import { LinkViewComponent } from './view/link-view/link-view.component';
import {MatTableModule} from '@angular/material/table';
import {MaterialModule} from '../../shared/material.module';
import {SharedModule} from '../../shared/shared.module';
import { CreateLinkDialogComponent } from './manage/create-link/create-link-dialog/create-link-dialog.component';
import {MatDialogModule} from '@angular/material/dialog';


@NgModule({
  declarations: [HomeComponent, LinkListComponent, LinkViewComponent, CreateLinkDialogComponent],
  imports: [
    CommonModule,
    HomeRoutingModule,
    MatTableModule,
    MaterialModule,
    SharedModule,
    MatDialogModule
  ],
  entryComponents: [CreateLinkDialogComponent]
})
export class HomeModule {
}
