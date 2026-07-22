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
  IonButton,
  IonSearchbar,
  IonSegment,
  IonSegmentButton,
  IonLabel,
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import {
  checkmarkCircle,
  timeOutline,
  alertCircle,
  calendarOutline,
  medicalOutline,
} from 'ionicons/icons';

import { ResumoVacinal, VaccinationSchedule } from '../../../models/vacina.model';
import { ChildService } from '../../../services/child/child.service';
import { VaccinationRecordService } from '../../../services/vaccination-record/vaccination-record.service';

@Component({
  selector: 'app-acompanhamento',
  templateUrl: './acompanhamento.page.html',
  styleUrls: ['./acompanhamento.page.scss'],
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
    IonButton,
    IonSearchbar,
    IonSegment,
    IonSegmentButton,
    IonLabel,
  ],
})
export class AcompanhamentoPage implements OnInit {
  private childService = inject(ChildService);
  private recordService = inject(VaccinationRecordService);

  criancaSelecionadaId: string | null = null;
  vacinas: VaccinationSchedule[] = [];
  resumo: ResumoVacinal = { total: 0, tomadas: 0, pendentes: 0, atrasadas: 0 };
  
  filtroStatus: string = 'todas';
  termoBusca: string = '';

  constructor() {
    addIcons({
      checkmarkCircle,
      timeOutline,
      alertCircle,
      calendarOutline,
      medicalOutline,
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
      this.resumo = { total: 0, tomadas: 0, pendentes: 0, atrasadas: 0 };
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
          v.doseName.toLowerCase().includes(termo)
      );
    }

    return filtradas;
  }

  marcarComoTomada(vacina: VaccinationSchedule) {
    if (!this.criancaSelecionadaId) return;

    const recordPayload = {
      childId: this.criancaSelecionadaId,
      vaccineId: vacina.vaccineId,
      doseId: vacina.doseId,
      appliedDate: new Date().toISOString().split('T')[0]
    };

    this.recordService.registerDose(recordPayload).subscribe({
      next: () => {
        this.carregarDados(); // Reload data after successful registration
      },
      error: (err) => {
        console.error('Erro ao registrar vacina', err);
      }
    });
  }

  getDiasParaVacina(vacina: VaccinationSchedule): number {
    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);
    const prevista = new Date(vacina.dueDate);
    prevista.setHours(0, 0, 0, 0);

    const diffTime = prevista.getTime() - hoje.getTime();
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  }
}
