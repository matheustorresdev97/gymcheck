import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { GymRequest, GymResponse } from '../models/gym.model';
import { PaginatedResponse } from '../models/pagination.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GymService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/gyms`;

  create(data: GymRequest): Observable<GymResponse> {
    return this.http.post<GymResponse>(this.apiUrl, data);
  }

  getById(id: string): Observable<GymResponse> {
    return this.http.get<GymResponse>(`${this.apiUrl}/${id}`);
  }

  update(id: string, data: GymRequest): Observable<GymResponse> {
    return this.http.put<GymResponse>(`${this.apiUrl}/${id}`, data);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  search(query: string, page = 0, size = 20): Observable<PaginatedResponse<GymResponse>> {
    const params = new HttpParams()
      .set('q', query)
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<PaginatedResponse<GymResponse>>(`${this.apiUrl}/search`, { params });
  }

  getNearby(latitude: number, longitude: number): Observable<GymResponse[]> {
    const params = new HttpParams()
      .set('latitude', latitude.toString())
      .set('longitude', longitude.toString());
    
    return this.http.get<GymResponse[]>(`${this.apiUrl}/nearby`, { params });
  }
}
