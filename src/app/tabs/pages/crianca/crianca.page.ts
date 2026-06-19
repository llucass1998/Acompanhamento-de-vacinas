import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {
  IonButton,
  IonCard,
  IonCardContent,
  IonContent,
  IonHeader,
  IonInput,
  IonItem,
  IonLabel,
  IonTitle,
  IonToolbar
} from '@ionic/angular/standalone';
import { Crianca } from '../../../models/crianca.model';
import { ResumoVacinal } from '../../../models/vacina.model';
import { VacinaService } from '../../../services/vacina.service';

@Component({
  selector: 'app-crianca',
  templateUrl: './crianca.page.html',
  styleUrls: ['./crianca.page.scss'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    IonButton,
    IonCard,
    IonCardContent,
    IonContent,
    IonHeader,
    IonInput,
    IonItem,
    IonLabel,
    IonTitle,
    IonToolbar
  ],
})
export class CriancaPage implements OnInit {
  criancas: Crianca[] = [];
  criancaSelecionada!: Crianca;
  resumosPorCrianca: Record<number, ResumoVacinal> = {};

  novaCrianca = {
    nome: '',
    dataNascimento: '',
    responsavel: '',
    observacao: ''
  };

  constructor(private vacinaService: VacinaService) {}

  ngOnInit() {
    this.carregarDados();
  }

  ionViewWillEnter() {
    this.carregarDados();
  }

  carregarDados() {
    this.criancas = this.vacinaService.getCriancas();
    this.criancaSelecionada = this.vacinaService.getCriancaSelecionada();
    this.resumosPorCrianca = this.criancas.reduce<Record<number, ResumoVacinal>>((resumos, crianca) => {
      resumos[crianca.id] = this.vacinaService.getResumoPorCrianca(crianca.id);
      return resumos;
    }, {});
  }

  selecionarCrianca(id: number) {
    this.vacinaService.selecionarCrianca(id);
    this.carregarDados();
  }

  adicionarCrianca() {
    if (!this.novaCrianca.nome || !this.novaCrianca.dataNascimento || !this.novaCrianca.responsavel) {
      return;
    }

    this.vacinaService.adicionarCrianca({
      nome: this.novaCrianca.nome,
      dataNascimento: this.novaCrianca.dataNascimento,
      responsavel: this.novaCrianca.responsavel,
      observacao: this.novaCrianca.observacao
    });

    this.novaCrianca = {
      nome: '',
      dataNascimento: '',
      responsavel: '',
      observacao: ''
    };

    this.carregarDados();
  }

  calcularIdade(dataNascimento: string): string {
    return this.vacinaService.calcularIdade(dataNascimento);
  }

  getResumo(criancaId: number): ResumoVacinal {
    return this.resumosPorCrianca[criancaId] ?? {
      total: 0,
      tomadas: 0,
      pendentes: 0,
      atrasadas: 0
    };
  }

  getSituacaoLabel(criancaId: number): string {
    const resumo = this.getResumo(criancaId);

    if (resumo.atrasadas > 0) {
      return `${resumo.atrasadas} vacina(s) atrasada(s)`;
    }

    if (resumo.pendentes > 0) {
      return `${resumo.pendentes} vacina(s) pendente(s)`;
    }

    return 'Carteira em dia';
  }

  getSituacaoClass(criancaId: number): string {
    const resumo = this.getResumo(criancaId);

    if (resumo.atrasadas > 0) {
      return 'status-atrasada';
    }

    if (resumo.pendentes > 0) {
      return 'status-pendente';
    }

    return 'status-tomada';
  }
}

