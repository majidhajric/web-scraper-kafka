import {Component, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {MatDialog, MatDialogConfig, MatDialogRef} from '@angular/material/dialog';
import {CreateLinkDialogComponent} from '../manage/create-link/create-link-dialog/create-link-dialog.component';
import {LinkListComponent} from './link-list/link-list.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  @ViewChild(LinkListComponent) linkListComponent: LinkListComponent;

  constructor(private router: Router, private dialog: MatDialog) {
  }

  ngOnInit(): void {
  }

  createLinkClick() {
    this.router.navigate(['new']);
  }

  openCreateLinkDialog() {

    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.minWidth = 600;
    dialogConfig.minHeight = 600;
    dialogConfig.maxWidth = 600;
    dialogConfig.maxHeight = 600;
    dialogConfig.panelClass = 'my-custom-dialog-class';

    const dialogRef = this.dialog.open(CreateLinkDialogComponent, dialogConfig)
      .afterClosed()
      .subscribe(() => {
        this.linkListComponent.loadLinks();
    });
  }


}
