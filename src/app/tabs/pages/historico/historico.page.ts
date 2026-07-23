import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import {
  IonContent,
  IonIcon,
} from '@ionic/angular/standalone';
import { addIcons } from 'ionicons';
import { documentTextOutline, timeOutline, checkmarkCircleOutline } from 'ionicons/icons';

import { VaccinationRecordService } from '../../../services/vaccination-record/vaccination-record.service';
import { ChildService } from '../../../services/child/child.service';
import { VaccinationSchedule } from '../../../models/vacina.model';
import { Crianca } from '../../../models/crianca.model';

import { PageHeaderComponent } from '../../../shared/components/page-header/page-header.component';
import { EmptyStateComponent } from '../../../shared/components/empty-state/empty-state.component';

@Component({
  selector: 'app-historico',
  templateUrl: './historico.page.html',
  styleUrls: ['./historico.page.scss'],
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule, 
    RouterModule,
    IonContent, 
    IonIcon, 
    PageHeaderComponent,
    EmptyStateComponent,
  ]
})
export class HistoricoPage implements OnInit {
  private recordService = inject(VaccinationRecordService);
  private childService = inject(ChildService);

  historico: VaccinationSchedule[] = [];
  criancas: Crianca[] = [];
  criancaSelecionadaId: string | null = null;
  filtroStatus: string = 'TODOS';

  constructor() {
    addIcons({ documentTextOutline, timeOutline, checkmarkCircleOutline });
  }

  ngOnInit() {
    this.carregarCriancas();
    this.childService.selectedChild$.subscribe(id => {
      this.criancaSelecionadaId = id;
      if (id) {
        this.carregarHistorico();
      }
    });
  }

  ionViewWillEnter() {
    if (this.criancaSelecionadaId) {
      this.carregarHistorico();
    } else {
      this.carregarCriancas();
    }
  }

  carregarCriancas() {
    this.childService.getChildren().subscribe(response => {
      this.criancas = response.content || [];
      if (this.criancas.length > 0 && !this.criancaSelecionadaId) {
        this.childService.selectChild(this.criancas[0].id);
      }
    });
  }

  onCriancaChange() {
    if (this.criancaSelecionadaId) {
      this.childService.selectChild(this.criancaSelecionadaId);
    }
  }

  onFiltroChange() {
    this.carregarHistorico();
  }

  carregarHistorico() {
    if (!this.criancaSelecionadaId) return;

    this.recordService.getChildRecords(this.criancaSelecionadaId).subscribe(response => {
      let dados: VaccinationSchedule[] = response.content || [];
      
      if (this.filtroStatus !== 'TODOS') {
        dados = dados.filter((r: VaccinationSchedule) => r.status === this.filtroStatus);
      }
      
      this.historico = dados.sort((a: VaccinationSchedule, b: VaccinationSchedule) => {
        const dataA = new Date(a.appliedDate || a.dueDate).getTime();
        const dataB = new Date(b.appliedDate || b.dueDate).getTime();
        return dataB - dataA;
      });
    });
  }
}
