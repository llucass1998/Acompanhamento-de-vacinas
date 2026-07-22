import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Campanha } from '../../models/vacina.model';

@Injectable({
  providedIn: 'root'
})
export class CampaignService {
  private readonly http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/campaigns`;

  public getCampaigns(): Observable<any> {
    return this.http.get<any>(this.API_URL);
  }
}
