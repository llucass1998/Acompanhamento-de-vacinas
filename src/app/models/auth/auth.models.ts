export interface UserResponse {
  id: string;
  name: string;
  email: string;
  role: string;
}

export interface JwtResponse {
  accessToken: string;
  refreshToken: string;
  user: UserResponse;
}
