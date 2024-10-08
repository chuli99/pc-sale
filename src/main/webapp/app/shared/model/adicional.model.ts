import { IDispositivo } from 'app/shared/model/dispositivo.model';

export interface IAdicional {
  id?: number;
  nombre?: string;
  descripcion?: string;
  precio?: number;
  precioGratis?: number;
  dispositivo?: IDispositivo;
}

export const defaultValue: Readonly<IAdicional> = {};
