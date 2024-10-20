import { IDispositivos } from 'app/entities/dispositivos/dispositivos.model';

export interface IAdicionales {
  id: number;
  nombre?: string | null;
  descripcion?: string | null;
  precio?: number | null;
  precioGratis?: number | null;
  dispositivos?: Pick<IDispositivos, 'id'> | null;
}

export type NewAdicionales = Omit<IAdicionales, 'id'> & { id: null };
