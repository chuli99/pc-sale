import { Moneda } from 'app/shared/model/enumerations/moneda.model';

export interface IDispositivo {
  id?: number;
  codigo?: string;
  nombre?: string;
  descripcion?: string;
  precioBase?: number;
  moneda?: Moneda;
}

export const defaultValue: Readonly<IDispositivo> = {};
