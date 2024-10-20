import { IOpciones, NewOpciones } from './opciones.model';

export const sampleWithRequiredData: IOpciones = {
  id: 72684,
  codigo: 'programming override',
  nombre: 'transmit convergence',
  descripcion: 'up',
  precioAdicional: 47490,
};

export const sampleWithPartialData: IOpciones = {
  id: 39003,
  codigo: 'deposit',
  nombre: 'methodologies Mauritius synthesize',
  descripcion: 'Producer',
  precioAdicional: 27150,
};

export const sampleWithFullData: IOpciones = {
  id: 63324,
  codigo: 'fuchsia Shoes',
  nombre: 'payment',
  descripcion: 'strategic state',
  precioAdicional: 29903,
};

export const sampleWithNewData: NewOpciones = {
  codigo: 'Savings Guadeloupe Sleek',
  nombre: 'Grass-roots Borders',
  descripcion: 'Connecticut hacking infomediaries',
  precioAdicional: 2482,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
