import { Injectable } from '@angular/core';
import { Crianca } from '../models/crianca.model';
import {
  Campanha,
  ResumoVacinal,
  StatusVacina,
  Vacina,
  VacinaComStatus
} from '../models/vacina.model';

@Injectable({
  providedIn: 'root'
})
export class VacinaService {
  private criancaSelecionadaKey = 'vacina-kids-crianca-selecionada';

  private criancas: Crianca[] = [
    {
      id: 1,
      nome: 'Ana Clara',
      dataNascimento: '2024-03-10',
      responsavel: 'Maria Souza',
      observacao: 'Acompanhamento de vacinas da primeira infância.'
    },
    {
      id: 2,
      nome: 'Lucas Gabriel',
      dataNascimento: '2021-08-22',
      responsavel: 'Maria Souza',
      observacao: 'Criança em fase escolar.'
    }
  ];

  private vacinas: Vacina[] = [
    {
      id: 1,
      criancaId: 1,
      nome: 'BCG',
      dose: 'Dose única',
      descricao: 'Vacina aplicada geralmente ao nascer.',
      dataPrevista: '2024-03-15',
      dataAplicacao: '2024-03-15',
      aplicada: true
    },
    {
      id: 2,
      criancaId: 1,
      nome: 'Hepatite B',
      dose: '1ª dose',
      descricao: 'Proteção contra Hepatite B.',
      dataPrevista: '2024-03-15',
      aplicada: false
    },
    {
      id: 3,
      criancaId: 1,
      nome: 'Pentavalente',
      dose: '1ª dose',
      descricao: 'Proteção contra difteria, tétano, coqueluche, hepatite B e Hib.',
      dataPrevista: '2026-07-20',
      aplicada: false
    },
    {
      id: 4,
      criancaId: 2,
      nome: 'Tríplice Viral',
      dose: '1ª dose',
      descricao: 'Proteção contra sarampo, caxumba e rubéola.',
      dataPrevista: '2022-08-22',
      dataAplicacao: '2022-08-25',
      aplicada: true
    },
    {
      id: 5,
      criancaId: 2,
      nome: 'Febre Amarela',
      dose: 'Reforço',
      descricao: 'Vacina indicada conforme calendário e região.',
      dataPrevista: '2025-08-22',
      aplicada: false
    },
    {
      id: 6,
      criancaId: 2,
      nome: 'Influenza',
      dose: 'Dose anual',
      descricao: 'Vacina contra gripe, conforme campanhas e calendário.',
      dataPrevista: '2026-08-10',
      aplicada: false
    }
  ];

  private campanhas: Campanha[] = [
    {
      id: 1,
      titulo: 'Campanha de Multivacinação',
      descricao: 'Atualização da caderneta de vacinação infantil.',
      publico: 'Crianças e adolescentes menores de 15 anos',
      periodo: '01/06 até 30/06',
      ativa: true
    },
    {
      id: 2,
      titulo: 'Campanha contra Influenza',
      descricao: 'Vacinação contra gripe para grupos prioritários.',
      publico: 'Crianças de 6 meses a menores de 6 anos',
      periodo: 'Consultar unidade de saúde',
      ativa: true
    },
    {
      id: 3,
      titulo: 'Campanha contra Poliomielite',
      descricao: 'Mobilização para reforço vacinal contra a poliomielite.',
      publico: 'Crianças menores de 5 anos',
      periodo: 'Campanha sazonal',
      ativa: false
    }
  ];

  getCriancas(): Crianca[] {
    return this.criancas;
  }

  getCriancaSelecionada(): Crianca {
    const idSalvo = Number(localStorage.getItem(this.criancaSelecionadaKey));
    const crianca = this.criancas.find(item => item.id === idSalvo);

    return crianca ?? this.criancas[0];
  }

  selecionarCrianca(id: number): void {
    localStorage.setItem(this.criancaSelecionadaKey, String(id));
  }

  adicionarCrianca(crianca: Omit<Crianca, 'id'>): void {
    const novoId = Math.max(...this.criancas.map(item => item.id)) + 1;

    this.criancas.push({
      id: novoId,
      ...crianca
    });

    this.selecionarCrianca(novoId);
  }

  getVacinasPorCrianca(criancaId: number): VacinaComStatus[] {
    return this.vacinas
      .filter(vacina => vacina.criancaId === criancaId)
      .map(vacina => ({
        ...vacina,
        status: this.calcularStatus(vacina)
      }));
  }

  getHistoricoPorCrianca(criancaId: number): VacinaComStatus[] {
    return this.getVacinasPorCrianca(criancaId)
      .filter(vacina => vacina.status === 'TOMADA');
  }

  getResumoPorCrianca(criancaId: number): ResumoVacinal {
    const vacinas = this.getVacinasPorCrianca(criancaId);

    return {
      total: vacinas.length,
      tomadas: vacinas.filter(vacina => vacina.status === 'TOMADA').length,
      pendentes: vacinas.filter(vacina => vacina.status === 'PENDENTE').length,
      atrasadas: vacinas.filter(vacina => vacina.status === 'ATRASADA').length
    };
  }

  marcarComoTomada(vacinaId: number): void {
    const vacina = this.vacinas.find(item => item.id === vacinaId);

    if (!vacina) {
      return;
    }

    vacina.aplicada = true;
    vacina.dataAplicacao = new Date().toISOString().split('T')[0];
  }

  getCampanhas(): Campanha[] {
    return this.campanhas;
  }

  calcularIdade(dataNascimento: string): string {
    const nascimento = new Date(dataNascimento);
    const hoje = new Date();

    let anos = hoje.getFullYear() - nascimento.getFullYear();
    let meses = hoje.getMonth() - nascimento.getMonth();

    if (hoje.getDate() < nascimento.getDate()) {
      meses--;
    }

    if (meses < 0) {
      anos--;
      meses += 12;
    }

    if (anos <= 0) {
      return `${meses} mês(es)`;
    }

    return `${anos} ano(s)`;
  }

  private calcularStatus(vacina: Vacina): StatusVacina {
    if (vacina.aplicada) {
      return 'TOMADA';
    }

    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);

    const dataPrevista = new Date(vacina.dataPrevista);
    dataPrevista.setHours(0, 0, 0, 0);

    if (dataPrevista < hoje) {
      return 'ATRASADA';
    }

    return 'PENDENTE';
  }
}
