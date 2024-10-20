import { IPersonalizaciones, NewPersonalizaciones } from './personalizaciones.model';

export const sampleWithRequiredData: IPersonalizaciones = {
  id: 13653,
  nombre: 'Lead Soap',
  descripcion: 'whiteboard Future-proofed Tools',
};

export const sampleWithPartialData: IPersonalizaciones = {
  id: 36191,
  nombre: 'Identity',
  descripcion: 'Cambridgeshire Concrete Fantastic',
};

export const sampleWithFullData: IPersonalizaciones = {
  id: 87440,
  nombre: 'Open-source',
  descripcion: 'approach fuchsia Bedfordshire',
};

export const sampleWithNewData: NewPersonalizaciones = {
  nombre: 'turquoise Inverse',
  descripcion: 'Cheese',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
