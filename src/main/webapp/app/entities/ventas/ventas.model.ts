import dayjs from 'dayjs/esm';
import { IDispositivos } from 'app/entities/dispositivos/dispositivos.model';

export interface IVentas {
  id: number;
  precioFinal?: number | null;
  fechaVenta?: dayjs.Dayjs | null;
  idDispositivo?: Pick<IDispositivos, 'id'> | null;
}

export type NewVentas = Omit<IVentas, 'id'> & { id: null };
