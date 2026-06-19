import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import {
  IonIcon,
  IonLabel,
  IonTabBar,
  IonTabButton,
  IonTabs
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  documentTextOutline,
  medkitOutline,
  megaphoneOutline,
  peopleOutline
} from 'ionicons/icons';

@Component({
  selector: 'app-tabs',
  templateUrl: './tabs.page.html',
  standalone: true,
  imports: [IonIcon, IonLabel, IonTabBar, IonTabButton, IonTabs, RouterLink],
})
export class TabsPage {
  constructor() {
    addIcons({
      documentTextOutline,
      medkitOutline,
      megaphoneOutline,
      peopleOutline
    });
  }
}
