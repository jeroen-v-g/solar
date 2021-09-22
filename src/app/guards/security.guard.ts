import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { SecurityService } from '../services/security.service';

@Injectable({
  providedIn: 'root'
})
export class SecurityGuard implements CanActivate {

  constructor(private securityService: SecurityService){}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      let routeConfig = route.routeConfig;
      if (routeConfig && routeConfig.path)
        return this.securityService.isAllowed(routeConfig.path);
      else
        return false;
  }

}
