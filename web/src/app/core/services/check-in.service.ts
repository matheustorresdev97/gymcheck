import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { CheckInRequest, CheckInResponse, UserMetricsResponse } from '../models/check-in.model';
import { PaginatedResponse } from '../models/pagination.model';

@Injectable({
  providedIn: 'root'
})
export class CheckInService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/check-ins';

  create(gymId: string, data: CheckInRequest): Observable<CheckInResponse> {
    return this.http.post<CheckInResponse>(`${this.apiUrl}/${gymId}`, data);
  }

  validate(checkInId: string): Observable<CheckInResponse> {
    return this.http.patch<CheckInResponse>(`${this.apiUrl}/validate/${checkInId}`, {});
  }

  getHistory(page = 0, size = 20, userId?: string): Observable<PaginatedResponse<CheckInResponse>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    if (userId) {
      params = params.set('userId', userId);
    }

    return this.http.get<PaginatedResponse<CheckInResponse>>(`${this.apiUrl}/history`, { params });
  }

  getMetrics(userId: string): Observable<UserMetricsResponse> {
    const params = new HttpParams().set('userId', userId);
    return this.http.get<UserMetricsResponse>(`${this.apiUrl}/metrics`, { params });
  }
}
