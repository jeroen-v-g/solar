import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { HttpClientModule } from '@angular/common/http';
import { DynamicMenuComponent } from './dynamic-menu/dynamic-menu.component';
import { MatMenuModule } from '@angular/material/menu'
import { MatButtonModule } from '@angular/material/button';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MenupatchDirective } from './directives/menupatch.directive';

@NgModule({
  declarations: [
    AppComponent,
    WelcomeComponent,
    DynamicMenuComponent,
    MenupatchDirective,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    MatMenuModule,
    MatButtonModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
