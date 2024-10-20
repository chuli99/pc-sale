import { IAdicionales, NewAdicionales } from './adicionales.model';

export const sampleWithRequiredData: IAdicionales = {
  id: 8940,
  nombre: 'Account',
  descripcion: 'circuit protocol',
  precio: 54639,
  precioGratis: 61115,
};

export const sampleWithPartialData: IAdicionales = {
  id: 38839,
  nombre: 'Washington application',
  descripcion: 'Chicken Loan',
  precio: 93905,
  precioGratis: 10279,
};

export const sampleWithFullData: IAdicionales = {
  id: 85863,
  nombre: 'Sleek',
  descripcion: 'bypassing red Metal',
  precio: 79321,
  precioGratis: 4084,
};

export const sampleWithNewData: NewAdicionales = {
  nombre: 'Frozen neutral',
  descripcion: 'AGP Junction',
  precio: 63542,
  precioGratis: 23326,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
