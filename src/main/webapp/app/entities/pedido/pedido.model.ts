import dayjs from 'dayjs/esm';

export interface IPedido {
  id?: number;
  fechaPedido?: dayjs.Dayjs | null;
  direccion?: string | null;
  estado?: string | null;
  infoDomicilio?: string | null;
  idDomiciliario?: number | null;
  idNotificacion?: number | null;
  idFactura?: number | null;
  userName?: string | null;
}

export class Pedido implements IPedido {
  constructor(
    public id?: number,
    public fechaPedido?: dayjs.Dayjs | null,
    public direccion?: string | null,
    public estado?: string | null,
    public infoDomicilio?: string | null,
    public idDomiciliario?: number | null,
    public idNotificacion?: number | null,
    public idFactura?: number | null,
    public userName?: string | null
  ) {}
}

export function getPedidoIdentifier(pedido: IPedido): number | undefined {
  return pedido.id;
}
