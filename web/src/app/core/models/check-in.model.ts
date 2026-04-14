export interface CheckInResponse {
  id: string;
  userId: string;
  gymId: string;
  gymTitle: string;
  createdAt: string;
  validatedAt?: string;
}

export interface CheckInRequest {
  latitude: number;
  longitude: number;
}

export interface UserMetricsResponse {
  checkInsCount: number;
}
