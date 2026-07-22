import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { VaccinationRecord, VaccinationSchedule, ResumoVacinal } from '../../models/vacina.model';

@Injectable({
  providedIn: 'root'
})
export class VaccinationRecordService {
  private readonly http = inject(HttpClient);
  private readonly SCHEDULES_API_URL = `${environment.apiUrl}/vaccination-schedules`;
  private readonly RECORDS_API_URL = `${environment.apiUrl}/vaccination-records`;

  public getChildSchedule(childId: string): Observable<any> {
    return this.http.get<any>(`${this.SCHEDULES_API_URL}/child/${childId}`);
  }

  public getChildSummary(childId: string): Observable<ResumoVacinal> {
    return this.http.get<ResumoVacinal>(`${this.SCHEDULES_API_URL}/child/${childId}/summary`);
  }

  public getChildRecords(childId: string): Observable<any> {
    return this.http.get<any>(`${this.RECORDS_API_URL}/child/${childId}`);
  }

  public registerDose(data: { childId: string, vaccineId: string, doseId: string, appliedDate: string, location?: string, notes?: string }): Observable<VaccinationRecord> {
    return this.http.post<VaccinationRecord>(this.RECORDS_API_URL, data);
  }
}
