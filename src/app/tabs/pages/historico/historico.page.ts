import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
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
export class HistoricoPage implements OnInit {
  crianca!: Crianca;
  historico: VacinaComStatus[] = [];

  constructor(private vacinaService: VacinaService) {}

  ngOnInit() {
    this.carregarDados();
  }

  ionViewWillEnter() {
    this.carregarDados();
  }

  carregarDados() {
    this.crianca = this.vacinaService.getCriancaSelecionada();
    this.historico = this.vacinaService.getHistoricoPorCrianca(this.crianca.id);
  }
}
