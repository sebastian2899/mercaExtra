import dayjs from 'dayjs/esm';

export interface ICaja {
  id?: number;
  fechaCreacion?: dayjs.Dayjs | null;
  valorTotalDia?: number | null;
  valorRegistradoDia?: number | null;
  diferencia?: number | null;
  estado?: string | null;
}

export class Caja implements ICaja {
  constructor(
    public id?: number,
    public fechaCreacion?: dayjs.Dayjs | null,
    public valorTotalDia?: number | null,
    public valorRegistradoDia?: number | null,
    public diferencia?: number | null,
    public estado?: string | null
  ) {}
}

export function getCajaIdentifier(caja: ICaja): number | undefined {
  return caja.id;
}
