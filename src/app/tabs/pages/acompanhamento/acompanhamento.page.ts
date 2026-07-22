import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import {
  IonButton,
  IonContent,
  IonHeader,
  IonTitle,
  IonToolbar
} from '@ionic/angular/standalone';

import { Crianca } from '../../../models/crianca.model';
import { ResumoVacinal, StatusVacina, VacinaComStatus } from '../../../models/vacina.model';
import { VacinaService } from '../../../services/vacina.service';

@Component({
  selector: 'app-acompanhamento',
  templateUrl: './acompanhamento.page.html',
  styleUrls: ['./acompanhamento.page.scss'],
  standalone: true,
  imports: [CommonModule, IonButton, IonContent, IonHeader, IonTitle, IonToolbar],
})
export class AcompanhamentoPage implements OnInit {
  private vacinaService = inject(VacinaService);

  crianca!: Crianca;
  vacinas: VacinaComStatus[] = [];
  resumo!: ResumoVacinal;
  percentualTomadas = 0;
  filtroStatus: StatusVacina | 'TODAS' = 'TODAS';

  ngOnInit() {
    this.carregarDados();
  }

  ionViewWillEnter() {
    this.carregarDados();
  }

  carregarDados() {
    this.crianca = this.vacinaService.getCriancaSelecionada();
    this.vacinas = this.vacinaService.getVacinasPorCrianca(this.crianca.id);
    this.resumo = this.vacinaService.getResumoPorCrianca(this.crianca.id);
    this.percentualTomadas = this.resumo.total > 0
      ? Math.round((this.resumo.tomadas / this.resumo.total) * 100)
      : 0;
  }

  marcarComoTomada(vacinaId: number) {
    this.vacinaService.marcarComoTomada(vacinaId);
    this.carregarDados();
  }

  get vacinasFiltradas(): VacinaComStatus[] {
    if (this.filtroStatus === 'TODAS') {
      return this.vacinas;
    }

    return this.vacinas.filter(vacina => vacina.status === this.filtroStatus);
  }

  getStatusClass(status: StatusVacina): string {
    return `status-${status.toLowerCase()}`;
  }

  getStatusLabel(status: StatusVacina): string {
    const labels: Record<StatusVacina, string> = {
      TOMADA: 'Tomada',
      PENDENTE: 'Pendente',
      ATRASADA: 'Atrasada'
    };

    return labels[status];
  }

  getDiasTexto(vacina: VacinaComStatus): string {
    const dias = this.vacinaService.getDiasParaVacina(vacina);

    if (vacina.status === 'TOMADA') {
      return 'Dose ja registrada.';
    }

    if (dias < 0) {
      return `${Math.abs(dias)} dia(s) de atraso.`;
    }

    if (dias === 0) {
      return 'Prevista para hoje.';
    }

    return `Faltam ${dias} dia(s).`;
  }

  getStatusDescription(status: StatusVacina): string {
    const descriptions: Record<StatusVacina, string> = {
      TOMADA: 'Dose ja registrada no historico vacinal.',
      PENDENTE: 'Dose prevista para uma data futura.',
      ATRASADA: 'Data prevista ultrapassada. Atencao necessaria.'
    };

    return descriptions[status];
  }
}
