import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { TopbarComponent } from './components/topbar/topbar.component';
import { NavMenuComponent } from './components/nav-menu/nav-menu.component';

@NgModule({
  declarations: [
    TopbarComponent,
    NavMenuComponent
  ],
  imports: [
    CommonModule,
    RouterModule
  ],
  exports: [
    TopbarComponent,
    NavMenuComponent,
    CommonModule,
    RouterModule
  ]
})
export class SharedModule { }
