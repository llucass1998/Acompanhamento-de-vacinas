import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import {
  IonContent,
  IonHeader,
  IonTitle,
  IonToolbar,
  IonButton,
  IonCard,
  IonIcon,
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  checkmarkCircle,
  timeOutline,
  alertCircle,
  calendarOutline,
  medicalOutline,
  peopleOutline,
  waterOutline
} from 'ionicons/icons';

import { VaccinationSummaryResponse, VaccinationScheduleResponse } from '../../../models/vacina.model';
import { ChildService } from '../../../services/child/child.service';
import { VaccinationRecordService } from '../../../services/vaccination-record/vaccination-record.service';
import { PageHeaderComponent } from '../../../shared/components/page-header/page-header.component';
import { EmptyStateComponent } from '../../../shared/components/empty-state/empty-state.component';
import { StatusChipComponent } from '../../../shared/components/status-chip/status-chip.component';

@Component({
  selector: 'app-acompanhamento',
  templateUrl: './acompanhamento.page.html',
  styleUrls: ['./acompanhamento.page.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    IonContent,
    IonButton,
    IonCard,
    IonIcon,
    PageHeaderComponent,
    EmptyStateComponent,
    StatusChipComponent
  ],
})
export class AcompanhamentoPage implements OnInit {
  private childService = inject(ChildService);
  private recordService = inject(VaccinationRecordService);

  criancaSelecionadaId: string | null = null;
  vacinas: VaccinationScheduleResponse[] = [];
  resumo: VaccinationSummaryResponse = { total: 0, taken: 0, pending: 0, overdue: 0, completionPercentage: 0 };
  
  filtroStatus: string = 'todas';
  termoBusca: string = '';

  constructor() {
    addIcons({
      checkmarkCircle,
      timeOutline,
      alertCircle,
      calendarOutline,
      medicalOutline,
      peopleOutline,
      waterOutline
    });
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
      this.vacinas = [];
      this.resumo = { total: 0, taken: 0, pending: 0, overdue: 0, completionPercentage: 0 };
      return;
    }

    this.recordService.getChildSchedule(this.criancaSelecionadaId).subscribe(
      (response) => {
        this.vacinas = response;
      }
    );

    this.recordService.getChildSummary(this.criancaSelecionadaId).subscribe(
      (summary) => {
        this.resumo = summary;
      }
    );
  }

  get vacinasFiltradas() {
    let filtradas = this.vacinas;

    if (this.filtroStatus !== 'todas') {
      filtradas = filtradas.filter(
        (v) => v.status === this.filtroStatus.toUpperCase()
      );
    }

    if (this.termoBusca && this.termoBusca.trim() !== '') {
      const termo = this.termoBusca.toLowerCase();
      filtradas = filtradas.filter(
        (v) =>
          v.vaccineName.toLowerCase().includes(termo) ||
          v.vaccineDose.doseName.toLowerCase().includes(termo)
      );
    }

    return filtradas;
  }

  marcarComoTomada(vacina: VaccinationScheduleResponse) {
    if (!this.criancaSelecionadaId) return;

    const recordPayload: any = {
      childId: this.criancaSelecionadaId,
      
      doseId: vacina.vaccineDose.id,
      appliedDate: new Date().toISOString().split('T')[0]
    };

    this.recordService.registerDose(this.criancaSelecionadaId, recordPayload).subscribe({
      next: () => {
        this.carregarDados();
      },
      error: (err) => {
        console.error('Erro ao registrar vacina', err);
      }
    });
  }

  mapStatusToChip(status: string): 'taken' | 'pending' | 'late' | 'default' {
    switch(status.toUpperCase()) {
      case 'TOMADA': return 'taken';
      case 'PENDENTE': return 'pending';
      case 'ATRASADA': return 'late';
      default: return 'default';
    }
  }
}
