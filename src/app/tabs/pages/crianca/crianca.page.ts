import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { Crianca } from '../../../models/crianca.model';
import { VacinaService } from '../../../services/vacina.service';

@Component({
  selector: 'app-crianca',
  templateUrl: './crianca.page.html',
  styleUrls: ['./crianca.page.scss'],
  standalone: true,
  imports: [IonicModule, CommonModule, FormsModule],
})
export class CriancaPage {
  criancas: Crianca[] = [];
  criancaSelecionada!: Crianca;

  novaCrianca = {
    nome: '',
    dataNascimento: '',
    responsavel: '',
    observacao: ''
  };

  constructor(private vacinaService: VacinaService) {}

  ionViewWillEnter() {
    this.carregarDados();
  }

  carregarDados() {
    this.criancas = this.vacinaService.getCriancas();
    this.criancaSelecionada = this.vacinaService.getCriancaSelecionada();
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
}