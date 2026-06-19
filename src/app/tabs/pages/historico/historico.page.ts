import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { IonicModule } from '@ionic/angular';
import { Crianca } from '../../../models/crianca.model';
import { VacinaComStatus } from '../../../models/vacina.model';
import { VacinaService } from '../../../services/vacina.service';

@Component({
  selector: 'app-historico',
  templateUrl: './historico.page.html',
  styleUrls: ['./historico.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule],
})
export class HistoricoPage {
  crianca!: Crianca;
  historico: VacinaComStatus[] = [];

  constructor(private vacinaService: VacinaService) {}

  ionViewWillEnter() {
    this.crianca = this.vacinaService.getCriancaSelecionada();
    this.historico = this.vacinaService.getHistoricoPorCrianca(this.crianca.id);
  }
}