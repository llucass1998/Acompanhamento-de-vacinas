export interface Crianca {
  id: string;
  name: string;
  birthDate: string;
  responsibleName: string;
  notes?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface ChildCreateRequest {
  name: string;
  birthDate: string; // "YYYY-MM-DD"
  responsibleName: string;
  notes?: string;
}

export interface ChildUpdateRequest {
  name: string;
  birthDate: string;
  responsibleName: string;
  notes?: string;
}
