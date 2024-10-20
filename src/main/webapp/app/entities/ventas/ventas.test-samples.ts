import dayjs from 'dayjs/esm';

import { IVentas, NewVentas } from './ventas.model';

export const sampleWithRequiredData: IVentas = {
  id: 2405,
  precioFinal: 93830,
  fechaVenta: dayjs('2024-10-17T08:17'),
};

export const sampleWithPartialData: IVentas = {
  id: 4923,
  precioFinal: 73448,
  fechaVenta: dayjs('2024-10-17T20:23'),
};

export const sampleWithFullData: IVentas = {
  id: 7106,
  precioFinal: 57088,
  fechaVenta: dayjs('2024-10-17T12:40'),
};

export const sampleWithNewData: NewVentas = {
  precioFinal: 61294,
  fechaVenta: dayjs('2024-10-17T03:49'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
