import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { Crianca } from '../../../models/crianca.model';
import { ResumoVacinal, VacinaComStatus } from '../../../models/vacina.model';
import { VacinaService } from '../../../services/vacina.service';

@Component({
  selector: 'app-acompanhamento',
  templateUrl: './acompanhamento.page.html',
  styleUrls: ['./acompanhamento.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule],
})
export class AcompanhamentoPage {
  crianca!: Crianca;
  vacinas: VacinaComStatus[] = [];
  resumo!: ResumoVacinal;

  constructor(private vacinaService: VacinaService) {}

  ionViewWillEnter() {
    this.carregarDados();
  }

  carregarDados() {
    this.crianca = this.vacinaService.getCriancaSelecionada();
    this.vacinas = this.vacinaService.getVacinasPorCrianca(this.crianca.id);
    this.resumo = this.vacinaService.getResumoPorCrianca(this.crianca.id);
  }

  marcarComoTomada(vacinaId: number) {
    this.vacinaService.marcarComoTomada(vacinaId);
    this.carregarDados();
  }

  getStatusClass(status: string): string {
    return `status-${status.toLowerCase()}`;
  }
}