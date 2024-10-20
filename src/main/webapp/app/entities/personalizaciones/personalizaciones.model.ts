import { IDispositivos } from 'app/entities/dispositivos/dispositivos.model';

export interface IPersonalizaciones {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  dispositivos?: Pick<IDispositivos, 'id'> | null;
}

export type NewPersonalizaciones = Omit<IPersonalizaciones, 'id'> & { id: null };
