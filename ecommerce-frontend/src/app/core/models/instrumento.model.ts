export interface Instrumento {
  id?: number;
  nombre: string;
  marca: string;
  tipo: string;
  precio: number;
  stock: number;
}

export interface User {
  username: string;
  token: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  success: boolean;
  token: string;
  user: string;
  message: string;
}

export interface Venta {
  id?: string;
  ventaId?: string;
  productoId: number;
  productoNombre: string;
  cantidad: number;
  precioUnitario: number;
  clienteNombre: string;
  clienteEmail: string;
  total?: number;
  fecha?: string;
}

export interface Promocion {
  id?: string;
  promocionId?: string;
  codigo: string;
  descripcion: string;
  tipoDescuento: string;
  valorDescuento: number;
  categoria: string;
  fechaInicio: string;
  fechaFin: string;
  stockLimite?: number;
  activa?: boolean;
}

export interface InventarioItem {
  id?: number;
  nombre: string;
  descripcion: string;
  precio: number;
  stockActual: number;
  stockMinimo: number;
  stockMaximo: number;
  categoria: string;
}

export interface EstadisticasPromocion {
  totalPromociones: number;
  promocionesActivas: number;
  promocionesExpiradas: number;
  categoriasMasPromovidas: any[];
}

export interface MonitorStats {
  ventasProcesadas: number;
  promocionesProcesadas: number;
  archivosGenerados: number;
  ultimaActualizacion: string;
}