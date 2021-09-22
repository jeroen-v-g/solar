import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';

export interface MenuItem {
  description: string,
  uri: string,
  route: boolean,
  children: MenuItem[]
}

@Injectable({
  providedIn: 'root'
})

export class MenuService {

  constructor(private httpClient: HttpClient) { }

  private getMenuUrl = environment.host+'menu/get';

  getMenu():Observable<MenuItem>{
    return this.httpClient.get<MenuItem>(`${this.getMenuUrl}`);
  }
}
