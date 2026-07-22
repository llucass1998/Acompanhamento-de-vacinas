import { Component, inject } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import {
  IonIcon,
  IonLabel,
  IonTabBar,
  IonTabButton,
  IonTabs,
  IonHeader,
  IonToolbar,
  IonTitle,
  IonButtons,
  IonButton
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  documentTextOutline,
  medkitOutline,
  megaphoneOutline,
  peopleOutline,
  logOutOutline
} from 'ionicons/icons';
import { AuthService } from '../services/auth/auth.service';

@Component({
  selector: 'app-tabs',
  templateUrl: './tabs.page.html',
  standalone: true,
  imports: [IonIcon, IonLabel, IonTabBar, IonTabButton, IonTabs, RouterLink, IonHeader, IonToolbar, IonTitle, IonButtons, IonButton],
})
export class TabsPage {
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  constructor() {
    addIcons({
      documentTextOutline,
      medkitOutline,
      megaphoneOutline,
      peopleOutline,
      logOutOutline
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
