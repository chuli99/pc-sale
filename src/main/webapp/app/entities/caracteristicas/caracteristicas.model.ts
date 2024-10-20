import { IDispositivos } from 'app/entities/dispositivos/dispositivos.model';

export interface ICaracteristicas {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  dispositivos?: Pick<IDispositivos, 'id'> | null;
}

export type NewCaracteristicas = Omit<ICaracteristicas, 'id'> & { id: null };
