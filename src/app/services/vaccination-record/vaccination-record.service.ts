import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { VaccinationRecordResponse, VaccinationScheduleResponse, VaccinationSummaryResponse, VaccinationRecordRequest } from '../../models/vacina.model';

@Injectable({
  providedIn: 'root'
})
export class VaccinationRecordService {
  private readonly http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/children`;

  public getChildSchedule(childId: string): Observable<VaccinationScheduleResponse[]> {
    return this.http.get<VaccinationScheduleResponse[]>(`${this.API_URL}/${childId}/vaccination-schedule`);
  }

  public getChildSummary(childId: string): Observable<VaccinationSummaryResponse> {
    return this.http.get<VaccinationSummaryResponse>(`${this.API_URL}/${childId}/vaccination-summary`);
  }

  public getChildRecords(childId: string): Observable<VaccinationRecordResponse[]> {
    return this.http.get<VaccinationRecordResponse[]>(`${this.API_URL}/${childId}/records`);
  }

  public registerDose(childId: string, data: VaccinationRecordRequest): Observable<VaccinationRecordResponse> {
    return this.http.post<VaccinationRecordResponse>(`${this.API_URL}/${childId}/records`, data);
  }
}
