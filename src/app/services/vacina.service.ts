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
  private readonly criancaSelecionadaKey = 'vacina-kids-crianca-selecionada';
  private readonly criancasStorageKey = 'vacina-kids-criancas';
  private readonly vacinasStorageKey = 'vacina-kids-vacinas';

  private criancas: Crianca[] = [
    {
      id: 1,
      nome: 'Ana Clara',
      dataNascimento: '2024-03-10',
      responsavel: 'Maria Souza',
      observacao: 'Acompanhamento de vacinas da primeira infancia.'
    },
    {
      id: 2,
      nome: 'Lucas Gabriel',
      dataNascimento: '2021-08-22',
      responsavel: 'Maria Souza',
      observacao: 'Crianca em fase escolar.'
    }
  ];

  private vacinas: Vacina[] = [
    {
      id: 1,
      criancaId: 1,
      nome: 'BCG',
      dose: 'Dose unica',
      descricao: 'Vacina aplicada geralmente ao nascer.',
      dataPrevista: '2024-03-15',
      dataAplicacao: '2024-03-15',
      aplicada: true
    },
    {
      id: 2,
      criancaId: 1,
      nome: 'Hepatite B',
      dose: 'Dose ao nascer',
      descricao: 'Protecao contra Hepatite B.',
      dataPrevista: '2024-03-15',
      aplicada: false
    },
    {
      id: 3,
      criancaId: 1,
      nome: 'Pentavalente',
      dose: '1a dose',
      descricao: 'Protecao contra difteria, tetano, coqueluche, hepatite B e Hib.',
      dataPrevista: '2024-05-10',
      aplicada: false
    },
    {
      id: 4,
      criancaId: 2,
      nome: 'Triplice Viral',
      dose: '1a dose',
      descricao: 'Protecao contra sarampo, caxumba e rubeola.',
      dataPrevista: '2022-08-22',
      dataAplicacao: '2022-08-25',
      aplicada: true
    },
    {
      id: 5,
      criancaId: 2,
      nome: 'Febre Amarela',
      dose: 'Dose inicial',
      descricao: 'Vacina indicada conforme calendario e regiao.',
      dataPrevista: '2022-05-22',
      aplicada: false
    },
    {
      id: 6,
      criancaId: 2,
      nome: 'Influenza',
      dose: 'Dose anual',
      descricao: 'Vacina contra gripe, conforme campanhas e calendario.',
      dataPrevista: '2026-08-10',
      aplicada: false
    }
  ];

  private campanhas: Campanha[] = [
    {
      id: 1,
      titulo: 'Campanha de Multivacinacao',
      descricao: 'Atualizacao da caderneta de vacinacao infantil.',
      publico: 'Criancas e adolescentes menores de 15 anos',
      periodo: 'Consultar unidade de saude',
      ativa: true
    },
    {
      id: 2,
      titulo: 'Campanha contra Influenza',
      descricao: 'Vacinacao contra gripe para grupos prioritarios.',
      publico: 'Criancas de 6 meses a menores de 6 anos',
      periodo: 'Campanha sazonal',
      ativa: true
    },
    {
      id: 3,
      titulo: 'Campanha contra Poliomielite',
      descricao: 'Mobilizacao para reforco vacinal contra a poliomielite.',
      publico: 'Criancas menores de 5 anos',
      periodo: 'Campanha sazonal',
      ativa: false
    }
  ];

  constructor() {
    this.carregarDadosSalvos();
  }

  getCriancas(): Crianca[] {
    return this.criancas.map(crianca => ({ ...crianca }));
  }

  getCriancaSelecionada(): Crianca {
    const idSalvo = Number(localStorage.getItem(this.criancaSelecionadaKey));
    const crianca = this.criancas.find(item => item.id === idSalvo);

    return { ...(crianca ?? this.criancas[0]) };
  }

  selecionarCrianca(id: number): void {
    if (this.criancas.some(item => item.id === id)) {
      localStorage.setItem(this.criancaSelecionadaKey, String(id));
    }
  }

  adicionarCrianca(crianca: Omit<Crianca, 'id'>): void {
    const novoId = this.getProximoCriancaId();

    this.criancas.push({
      id: novoId,
      ...crianca
    });

    this.vacinas.push(...this.criarCalendarioInicial(novoId, crianca.dataNascimento));
    this.selecionarCrianca(novoId);
    this.salvarDados();
  }

  getVacinasPorCrianca(criancaId: number): VacinaComStatus[] {
    return this.vacinas
      .filter(vacina => vacina.criancaId === criancaId)
      .map(vacina => ({
        ...vacina,
        status: this.calcularStatus(vacina)
      }))
      .sort((a, b) => a.dataPrevista.localeCompare(b.dataPrevista));
  }

  getHistoricoPorCrianca(criancaId: number): VacinaComStatus[] {
    return this.getVacinasPorCrianca(criancaId)
      .filter(vacina => vacina.status === 'TOMADA')
      .sort((a, b) => (b.dataAplicacao ?? '').localeCompare(a.dataAplicacao ?? ''));
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

  getDiasParaVacina(vacina: Pick<Vacina, 'dataPrevista'>): number {
    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);

    const prevista = new Date(vacina.dataPrevista);
    prevista.setHours(0, 0, 0, 0);

    return Math.ceil((prevista.getTime() - hoje.getTime()) / (1000 * 60 * 60 * 24));
  }

  marcarComoTomada(vacinaId: number): void {
    const vacina = this.vacinas.find(item => item.id === vacinaId);

    if (!vacina) {
      return;
    }

    vacina.aplicada = true;
    vacina.dataAplicacao = new Date().toISOString().split('T')[0];
    this.salvarDados();
  }

  getCampanhas(): Campanha[] {
    return this.campanhas.map(campanha => ({ ...campanha }));
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
      return `${Math.max(meses, 0)} mes(es)`;
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

  private carregarDadosSalvos(): void {
    const criancasSalvas = this.lerStorage<Crianca[]>(this.criancasStorageKey);
    const vacinasSalvas = this.lerStorage<Vacina[]>(this.vacinasStorageKey);

    if (criancasSalvas?.length) {
      this.criancas = criancasSalvas;
    }

    if (vacinasSalvas?.length) {
      this.vacinas = vacinasSalvas;
    }

    if (!localStorage.getItem(this.criancaSelecionadaKey) && this.criancas.length) {
      this.selecionarCrianca(this.criancas[0].id);
    }
  }

  private salvarDados(): void {
    localStorage.setItem(this.criancasStorageKey, JSON.stringify(this.criancas));
    localStorage.setItem(this.vacinasStorageKey, JSON.stringify(this.vacinas));
  }

  private lerStorage<T>(key: string): T | null {
    const valor = localStorage.getItem(key);

    if (!valor) {
      return null;
    }

    try {
      return JSON.parse(valor) as T;
    } catch {
      return null;
    }
  }

  private getProximoCriancaId(): number {
    if (!this.criancas.length) {
      return 1;
    }

    return Math.max(...this.criancas.map(item => item.id)) + 1;
  }

  private getProximoVacinaId(): number {
    if (!this.vacinas.length) {
      return 1;
    }

    return Math.max(...this.vacinas.map(item => item.id)) + 1;
  }

  private criarCalendarioInicial(criancaId: number, dataNascimento: string): Vacina[] {
    let proximoId = this.getProximoVacinaId();

    const criarVacina = (
      nome: string,
      dose: string,
      descricao: string,
      mesesAposNascimento: number
    ): Vacina => ({
      id: proximoId++,
      criancaId,
      nome,
      dose,
      descricao,
      dataPrevista: this.somarMeses(dataNascimento, mesesAposNascimento),
      aplicada: false
    });

    return [
      criarVacina('BCG', 'Dose unica', 'Protecao contra formas graves de tuberculose.', 0),
      criarVacina('Hepatite B', 'Dose ao nascer', 'Protecao contra Hepatite B.', 0),
      criarVacina('Pentavalente', '1a dose', 'Difteria, tetano, coqueluche, hepatite B e Hib.', 2),
      criarVacina('VIP', '1a dose', 'Vacina inativada contra poliomielite.', 2),
      criarVacina('Pneumococica 10-valente', '1a dose', 'Protecao contra doencas pneumococicas.', 2),
      criarVacina('Rotavirus', '1a dose', 'Protecao contra formas graves de gastroenterite por rotavirus.', 2),
      criarVacina('Meningococica C', '1a dose', 'Protecao contra doenca meningococica C.', 3),
      criarVacina('Pentavalente', '2a dose', 'Difteria, tetano, coqueluche, hepatite B e Hib.', 4),
      criarVacina('VIP', '2a dose', 'Vacina inativada contra poliomielite.', 4),
      criarVacina('Pneumococica 10-valente', '2a dose', 'Protecao contra doencas pneumococicas.', 4),
      criarVacina('Rotavirus', '2a dose', 'Protecao contra formas graves de gastroenterite por rotavirus.', 4),
      criarVacina('Meningococica C', '2a dose', 'Protecao contra doenca meningococica C.', 5),
      criarVacina('Pentavalente', '3a dose', 'Difteria, tetano, coqueluche, hepatite B e Hib.', 6),
      criarVacina('VIP', '3a dose', 'Vacina inativada contra poliomielite.', 6),
      criarVacina('Influenza', 'Dose anual', 'Vacina contra gripe, conforme campanha anual.', 6),
      criarVacina('Febre Amarela', 'Dose inicial', 'Indicada conforme calendario e orientacao de saude.', 9),
      criarVacina('Triplice Viral', '1a dose', 'Protecao contra sarampo, caxumba e rubeola.', 12),
      criarVacina('Pneumococica 10-valente', 'Reforco', 'Reforco contra doencas pneumococicas.', 12),
      criarVacina('Meningococica C', 'Reforco', 'Reforco contra doenca meningococica C.', 12),
      criarVacina('DTP', '1o reforco', 'Reforco contra difteria, tetano e coqueluche.', 15),
      criarVacina('VOP', '1o reforco', 'Reforco oral contra poliomielite.', 15),
      criarVacina('Hepatite A', 'Dose unica', 'Protecao contra Hepatite A.', 15),
      criarVacina('Tetra Viral', 'Dose unica', 'Sarampo, caxumba, rubeola e varicela.', 15),
      criarVacina('DTP', '2o reforco', 'Reforco contra difteria, tetano e coqueluche.', 48),
      criarVacina('VOP', '2o reforco', 'Reforco oral contra poliomielite.', 48),
      criarVacina('Varicela', 'Dose de reforco', 'Protecao contra varicela conforme calendario.', 48)
    ];
  }

  private somarMeses(data: string, meses: number): string {
    const [ano, mes, dia] = data.split('-').map(Number);
    const resultado = new Date(ano, mes - 1 + meses, dia);

    return [
      resultado.getFullYear(),
      String(resultado.getMonth() + 1).padStart(2, '0'),
      String(resultado.getDate()).padStart(2, '0')
    ].join('-');
  }
}
