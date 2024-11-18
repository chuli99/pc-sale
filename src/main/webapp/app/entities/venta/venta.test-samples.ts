import dayjs from 'dayjs/esm';

import { IVenta, NewVenta } from './venta.model';

export const sampleWithRequiredData: IVenta = {
  id: 4224,
  fechaVenta: dayjs('2024-11-18T01:00'),
  precioFinal: 22751.84,
};

export const sampleWithPartialData: IVenta = {
  id: 13286,
  fechaVenta: dayjs('2024-11-18T01:53'),
  precioFinal: 1762.26,
};

export const sampleWithFullData: IVenta = {
  id: 15032,
  fechaVenta: dayjs('2024-11-18T05:42'),
  precioFinal: 31425.03,
};

export const sampleWithNewData: NewVenta = {
  fechaVenta: dayjs('2024-11-18T08:51'),
  precioFinal: 6504.23,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
