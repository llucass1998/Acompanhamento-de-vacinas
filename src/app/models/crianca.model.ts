export interface Crianca {
  id: string;
  name: string;
  birthDate: string;
  gender: string;
  notes?: string;
  active: boolean;
}

export interface ChildCreateRequest {
  name: string;
  birthDate: string; // "YYYY-MM-DD"
  gender: string;
  notes?: string;
}

export interface ChildUpdateRequest {
  name: string;
  birthDate: string;
  gender: string;
  notes?: string;
}
