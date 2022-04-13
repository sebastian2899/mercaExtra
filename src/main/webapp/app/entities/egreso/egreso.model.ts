import dayjs from 'dayjs/esm';

export interface IEgreso {
  id?: number;
  fechaCreacion?: dayjs.Dayjs | null;
  descripcion?: string | null;
  valorEgreso?: number | null;
}

export class Egreso implements IEgreso {
  constructor(
    public id?: number,
    public fechaCreacion?: dayjs.Dayjs | null,
    public descripcion?: string | null,
    public valorEgreso?: number | null
  ) {}
}

export function getEgresoIdentifier(egreso: IEgreso): number | undefined {
  return egreso.id;
}
