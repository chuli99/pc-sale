import { ICaracteristicas, NewCaracteristicas } from './caracteristicas.model';

export const sampleWithRequiredData: ICaracteristicas = {
  id: 61975,
  nombre: 'Berkshire pink 1080p',
  descripcion: 'Massachusetts',
};

export const sampleWithPartialData: ICaracteristicas = {
  id: 78029,
  nombre: 'Soft Producer empower',
  descripcion: 'cross-platform',
};

export const sampleWithFullData: ICaracteristicas = {
  id: 69649,
  nombre: 'Street',
  descripcion: 'robust',
};

export const sampleWithNewData: NewCaracteristicas = {
  nombre: 'Branding Fresh connecting',
  descripcion: 'Plastic',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
