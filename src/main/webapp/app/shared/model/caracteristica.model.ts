import { IDispositivo } from 'app/shared/model/dispositivo.model';

export interface ICaracteristica {
  id?: number;
  nombre?: string;
  descripcion?: string;
  dispositivo?: IDispositivo;
}

export const defaultValue: Readonly<ICaracteristica> = {};
