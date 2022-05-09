import dayjs from 'dayjs';

export interface IReembolso {
  id?: number;
  idPedido?: number | null;
  idDomiciliario?: number | null;
  idFactura?: number | null;
  descripcion?: string | null;
  estado?: string | null;
  fechaReembolso?: dayjs.Dayjs | null;
}

export class Reembolso implements IReembolso {
  constructor(
    public id?: number,
    public idPedido?: number | null,
    public idDomiciliario?: number | null,
    public idFactura?: number | null,
    public descripcion?: string | null,
    public estado?: string | null,
    public fechaReembolso?: dayjs.Dayjs | null
  ) {}
}

export function getReembolsoIdentifier(reembolso: IReembolso): number | undefined {
  return reembolso.id;
}
