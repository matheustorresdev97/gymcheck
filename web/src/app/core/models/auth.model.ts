import { UserResponse } from "./user.model";

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  user: UserResponse;
  token: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
}

export interface RefreshTokenResponse {
  token: string;
}
