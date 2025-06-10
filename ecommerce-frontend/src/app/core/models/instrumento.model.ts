export interface Instrumento {
  id?: number;
  nombre: string;
  marca: string;
  tipo: string;
  precio: number;
  stock: number;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  success: boolean;
  message: string;
  user?: string;
  token?: string;
}

export interface User {
  username: string;
  token: string;
}