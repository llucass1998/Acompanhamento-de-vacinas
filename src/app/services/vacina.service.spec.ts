import { TestBed } from '@angular/core/testing';
import { VacinaService } from './vacina.service';

describe('VacinaService', () => {
  let service: VacinaService;

  beforeEach(() => {
    localStorage.clear();
    TestBed.configureTestingModule({});
    service = TestBed.inject(VacinaService);
  });

  afterEach(() => {
    localStorage.clear();
  });

  it('deve cadastrar uma crianca e criar vacinas previstas para acompanhamento', () => {
    service.adicionarCrianca({
      nome: 'Pedro Henrique',
      dataNascimento: '2025-01-15',
      responsavel: 'Lucas Souza',
      observacao: 'Teste de acompanhamento'
    });

    const criancaSelecionada = service.getCriancaSelecionada();
    const vacinas = service.getVacinasPorCrianca(criancaSelecionada.id);
    const resumo = service.getResumoPorCrianca(criancaSelecionada.id);

    expect(criancaSelecionada.nome).toBe('Pedro Henrique');
    expect(vacinas.length).toBeGreaterThan(0);
    expect(resumo.total).toBe(vacinas.length);
    expect(resumo.pendentes + resumo.atrasadas).toBe(resumo.total);
  });

  it('deve identificar vacina atrasada quando a data prevista ja passou', () => {
    const crianca = service.getCriancaSelecionada();
    const vacinas = service.getVacinasPorCrianca(crianca.id);

    expect(vacinas.some(vacina => vacina.status === 'ATRASADA')).toBeTrue();
  });

  it('deve registrar vacina como tomada e exibir no historico da crianca', () => {
    const crianca = service.getCriancaSelecionada();
    const vacinaPendente = service.getVacinasPorCrianca(crianca.id)
      .find(vacina => vacina.status !== 'TOMADA');

    expect(vacinaPendente).toBeTruthy();

    service.marcarComoTomada(vacinaPendente!.id);

    const historico = service.getHistoricoPorCrianca(crianca.id);
    const vacinaNoHistorico = historico.find(vacina => vacina.id === vacinaPendente!.id);

    expect(vacinaNoHistorico?.status).toBe('TOMADA');
    expect(vacinaNoHistorico?.dataAplicacao).toBeTruthy();
  });

  it('deve manter acompanhamento separado para criancas diferentes', () => {
    const primeiraCrianca = service.getCriancas()[0];
    const segundaCrianca = service.getCriancas()[1];

    service.selecionarCrianca(primeiraCrianca.id);
    const vacinaPrimeiraCrianca = service.getVacinasPorCrianca(primeiraCrianca.id)
      .find(vacina => vacina.status !== 'TOMADA');

    expect(vacinaPrimeiraCrianca).toBeTruthy();

    service.marcarComoTomada(vacinaPrimeiraCrianca!.id);

    const historicoPrimeira = service.getHistoricoPorCrianca(primeiraCrianca.id);
    const historicoSegunda = service.getHistoricoPorCrianca(segundaCrianca.id);

    expect(historicoPrimeira.some(vacina => vacina.id === vacinaPrimeiraCrianca!.id)).toBeTrue();
    expect(historicoSegunda.some(vacina => vacina.id === vacinaPrimeiraCrianca!.id)).toBeFalse();
  });

  it('deve exibir campanhas de vacinacao cadastradas', () => {
    const campanhas = service.getCampanhas();

    expect(campanhas.length).toBeGreaterThan(0);
    expect(campanhas.some(campanha => campanha.ativa)).toBeTrue();
  });
});
