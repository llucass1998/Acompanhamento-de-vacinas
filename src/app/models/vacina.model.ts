export type StatusVacina = 'TOMADA' | 'PENDENTE' | 'ATRASADA';

export interface Vacina {
  id: number;
  criancaId: number;
  nome: string;
  dose: string;
  descricao: string;
  dataPrevista: string;
  dataAplicacao?: string;
  aplicada: boolean;
}

export interface VacinaComStatus extends Vacina {
  status: StatusVacina;
}

export interface Campanha {
  id: number;
  titulo: string;
  descricao: string;
  publico: string;
  periodo: string;
  ativa: boolean;
}

export interface ResumoVacinal {
  total: number;
  tomadas: number;
  pendentes: number;
  atrasadas: number;
}