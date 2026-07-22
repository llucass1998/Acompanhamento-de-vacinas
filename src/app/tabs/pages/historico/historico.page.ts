import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  IonContent,
  IonHeader,
  IonTitle,
  IonToolbar,
  IonCard,
  IonCardHeader,
  IonCardTitle,
  IonCardContent,
  IonBadge,
  IonIcon,
  IonLabel,
  IonButton,
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import { checkmarkCircle, locationOutline, calendarOutline, documentTextOutline, peopleOutline } from 'ionicons/icons';

import { VaccinationRecord } from '../../../models/vacina.model';
import { ChildService } from '../../../services/child/child.service';
import { VaccinationRecordService } from '../../../services/vaccination-record/vaccination-record.service';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-historico',
  templateUrl: './historico.page.html',
  styleUrls: ['./historico.page.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    IonContent,
    IonHeader,
    IonTitle,
    IonToolbar,
    IonCard,
    IonCardHeader,
    IonCardTitle,
    IonCardContent,
    IonBadge,
    IonIcon,
    IonLabel,
    IonButton,
    RouterModule,
  ],
})
export class HistoricoPage implements OnInit {
  private childService = inject(ChildService);
  private recordService = inject(VaccinationRecordService);

  criancaSelecionadaId: string | null = null;
  historico: any[] = [];

  constructor() {
    addIcons({ checkmarkCircle, locationOutline, calendarOutline, documentTextOutline, peopleOutline });
  }

  ngOnInit() {
    this.childService.selectedChild$.subscribe(id => {
      this.criancaSelecionadaId = id;
      this.carregarDados();
    });
  }

  ionViewWillEnter() {
    this.carregarDados();
  }

  carregarDados() {
    if (!this.criancaSelecionadaId) {
      this.historico = [];
      return;
    }

    this.recordService.getChildRecords(this.criancaSelecionadaId).subscribe(
      (response) => {
        // Sort by applied date descending
        this.historico = response.sort((a: any, b: any) => 
          new Date(b.appliedDate).getTime() - new Date(a.appliedDate).getTime()
        );
      }
    );
  }
}
