export interface GymResponse {
  id: string;
  title: string;
  description: string;
  phone: string;
  latitude: number;
  longitude: number;
}

export interface GymRequest {
  title: string;
  description?: string;
  phone?: string;
  latitude: number;
  longitude: number;
}
