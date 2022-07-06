import { Directive, Input, ContentChildren, QueryList } from '@angular/core';
import { MatMenuItem } from '@angular/material/menu';

@Directive({
  selector: '[appMenupatch]'
})
export class MenupatchDirective {
  @ContentChildren(MatMenuItem, {descendants: true}) _allItems: QueryList<MatMenuItem>;
  @Input() parentMenu: any;
  constructor() {
  }

  ngAfterViewChecked(){
    this._allItems.forEach(item=>{
      item._parentMenu=this.parentMenu;
    })
    this.parentMenu._allItems=this._allItems;
    this.parentMenu._updateDirectDescendants();
  }

}
