import { IPersonalizaciones } from 'app/entities/personalizaciones/personalizaciones.model';

export interface IOpciones {
  id: number;
  codigo?: string | null;
  nombre?: string | null;
  descripcion?: string | null;
  precioAdicional?: number | null;
  personalizaciones?: Pick<IPersonalizaciones, 'id'> | null;
}

export type NewOpciones = Omit<IOpciones, 'id'> & { id: null };
