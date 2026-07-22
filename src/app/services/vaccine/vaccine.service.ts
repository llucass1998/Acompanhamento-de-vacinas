import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Vaccine } from '../../models/vacina.model';

@Injectable({
  providedIn: 'root'
})
export class VaccineService {
  private readonly http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/vaccines`;

  public getVaccines(): Observable<any> {
    return this.http.get<any>(this.API_URL);
  }

  public getVaccine(id: string): Observable<Vaccine> {
    return this.http.get<Vaccine>(`${this.API_URL}/${id}`);
  }
}
