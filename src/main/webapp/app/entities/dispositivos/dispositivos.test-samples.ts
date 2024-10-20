import { IDispositivos, NewDispositivos } from './dispositivos.model';

export const sampleWithRequiredData: IDispositivos = {
  id: 34196,
  codigo: 'Consultant Keyboard Home',
  nombre: 'Account scalable',
  descripcion: 'reboot capacitor RSS',
  precioBase: 78526,
  moneda: 'Borders',
};

export const sampleWithPartialData: IDispositivos = {
  id: 56334,
  codigo: 'Shoes Franc AI',
  nombre: 'Tools Legacy Azerbaijan',
  descripcion: 'Proactive Computer',
  precioBase: 61993,
  moneda: 'South Wooden copy',
};

export const sampleWithFullData: IDispositivos = {
  id: 66671,
  codigo: 'Reactive indigo Baht',
  nombre: 'plug-and-play Front-line',
  descripcion: 'invoice invoice Awesome',
  precioBase: 23769,
  moneda: '1080p',
};

export const sampleWithNewData: NewDispositivos = {
  codigo: 'Delaware fuchsia Pants',
  nombre: 'action-items deposit',
  descripcion: 'implementation',
  precioBase: 58949,
  moneda: 'Israel virtual Chief',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
