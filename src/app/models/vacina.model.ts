export type StatusVacina = 'TOMADA' | 'PENDENTE' | 'ATRASADA';

export interface Vaccine {
  id: string;
  name: string;
  description: string;
  recommendedAgeMonths: number;
}

export interface VaccineDose {
  id: string;
  vaccineId: string;
  doseName: string;
  recommendedAgeMonths: number;
}

export interface VaccinationSchedule {
  id: string;
  childId: string;
  vaccineId: string;
  vaccineName: string;
  doseId: string;
  doseName: string;
  dueDate: string; // YYYY-MM-DD
  status: StatusVacina;
  appliedDate?: string;
}

export interface VaccinationRecord {
  id: string;
  childId: string;
  vaccineId: string;
  doseId: string;
  appliedDate: string;
  location?: string;
  notes?: string;
}

export interface ResumoVacinal {
  total: number;
  tomadas: number;
  pendentes: number;
  atrasadas: number;
}

export interface Campanha {
  id: string;
  title: string;
  description: string;
  targetAudience: string;
  startDate: string;
  endDate: string;
  active: boolean;
}
