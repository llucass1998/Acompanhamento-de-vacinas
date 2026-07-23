import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  IonContent,
  IonCard,
  IonIcon,
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import { calendarOutline, peopleOutline } from 'ionicons/icons';

import { Campanha } from '../../../models/vacina.model';
import { CampaignService } from '../../../services/campaign/campaign.service';

import { PageHeaderComponent } from '../../../shared/components/page-header/page-header.component';
import { EmptyStateComponent } from '../../../shared/components/empty-state/empty-state.component';

@Component({
  selector: 'app-campanhas',
  templateUrl: './campanhas.page.html',
  styleUrls: ['./campanhas.page.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    IonContent,
    IonCard,
    IonIcon,
    PageHeaderComponent,
    EmptyStateComponent
  ],
})
export class CampanhasPage implements OnInit {
  private campaignService = inject(CampaignService);
  campanhas: Campanha[] = [];

  constructor() {
    addIcons({ calendarOutline, peopleOutline });
  }

  ngOnInit() {
    this.carregarDados();
  }

  ionViewWillEnter() {
    this.carregarDados();
  }

  carregarDados() {
    this.campaignService.getCampaigns().subscribe(
      (response) => {
        // Handle pagination response format
        this.campanhas = response.content || response || [];
      }
    );
  }
}
