export type StatusVacina = 'TAKEN' | 'PENDENTE' | 'OVERDUE' | 'PENDING';

export interface Vaccine {
  id: string;
  name: string;
  description: string;
  active: boolean;
  code: string;
  displayName: string;
  official: boolean;
}

export interface VaccineDoseResponse {
  id: string;
  vaccineId: string;
  doseName: string;
  recommendedAgeMonths: number;
  description: string;
  code: string;
}

export interface VaccinationScheduleResponse {
  id: string;
  childId: string;
  vaccineDose: VaccineDoseResponse;
  vaccineName: string;
  expectedDate: string; // YYYY-MM-DD
  status: StatusVacina;
}

export interface VaccinationRecordResponse {
  id: string;
  childId: string;
  doseId: string;
  vaccineName: string;
  doseName: string;
  appliedDate: string;
  location?: string;
  batchNumber?: string;
  observations?: string;
  proofUrl?: string;
}

export interface VaccinationRecordRequest {
  doseId: string;
  appliedDate: string;
  location?: string;
  batchNumber?: string;
  observations?: string;
}

export interface VaccinationSummaryResponse {
  total: number;
  taken: number;
  pending: number;
  overdue: number;
  completionPercentage: number;
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
