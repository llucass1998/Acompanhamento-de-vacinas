import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { IonicModule } from '@ionic/angular';
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
  imports: [IonicModule, RouterLink],
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
