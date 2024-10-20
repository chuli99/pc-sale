export interface IDispositivos {
  id: number;
  codigo?: string | null;
  nombre?: string | null;
  descripcion?: string | null;
  precioBase?: number | null;
  moneda?: string | null;
}

export type NewDispositivos = Omit<IDispositivos, 'id'> & { id: null };
