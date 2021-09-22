import { Component, OnInit, ViewChild, ElementRef, QueryList } from '@angular/core';
import { MatMenu, MatMenuItem, MatMenuPanel, MatMenuTrigger, _MatMenuBase } from '@angular/material/menu';
import { MenuService, MenuItem } from '../services/menu.service';

@Component({
  selector: 'app-dynamic-menu',
  templateUrl: './dynamic-menu.component.html',
  styleUrls: ['./dynamic-menu.component.css'],
})
export class DynamicMenuComponent implements OnInit {
  public startMenuItem: MenuItem = {
    description: 'Menu',
    uri: '',
    route: false,
    children: []
   };

  constructor(private menuService: MenuService) {}

  public onOpenUrl(url: string) {
    document.location.href = url;
  }

  public hasSubItems(item: MenuItem):boolean{
    return (Array.isArray(item.children) && item.children.length>0);
  }

  public patch(item: MatMenu, triggerbutton: MatMenuItem, trigger: any, parentMenu: MatMenuPanel):MatMenu
  {
    if (parentMenu)
    {
        triggerbutton._triggersSubmenu=true;
        trigger._parentMaterialMenu=parentMenu;
    }
    return item;
  }

  ngOnInit(): void {
    this.menuService.getMenu().subscribe((menuItem) => {
      this.startMenuItem = menuItem;
    });
  }
}
