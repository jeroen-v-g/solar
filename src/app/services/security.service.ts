import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

type apiResponse = {
  allowed: boolean;
}

@Injectable({
  providedIn: 'root'
})

export class SecurityService {

  constructor(private httpClient: HttpClient) { }

  private isAllowedUrl = environment.host+'security/isAllowed';

  isAllowed(route: string):Promise<boolean>{
    return this.httpClient.get<apiResponse>(`${this.isAllowedUrl}?url=${route}`).toPromise().then(value=>{
      return value.allowed;
    })
  }
}
