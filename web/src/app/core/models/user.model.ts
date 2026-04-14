export interface UserResponse {
  id: string;
  name: string;
  email: string;
  roles: string[];
  createdAt: string;
}

export interface UserUpdateRequest {
  name?: string;
  email?: string;
}
