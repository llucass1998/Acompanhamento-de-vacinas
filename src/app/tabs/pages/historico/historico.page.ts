import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import {
  IonContent,
  IonHeader,
  IonTitle,
  IonToolbar
} from '@ionic/angular/standalone';
import { Crianca } from '../../../models/crianca.model';
import { VacinaComStatus } from '../../../models/vacina.model';
import { VacinaService } from '../../../services/vacina.service';

@Component({
  selector: 'app-historico',
  templateUrl: './historico.page.html',
  styleUrls: ['./historico.page.scss'],
  standalone: true,
  imports: [CommonModule, IonContent, IonHeader, IonTitle, IonToolbar],
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
