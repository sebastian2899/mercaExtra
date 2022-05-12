export interface IDatosPedidoReembolso {
  fechaPedido?: string | null;
  valorFactura?: number | null;
  pedidoDireccion?: string | null;
  domiciliario?: string | null;
  idPedido?: number | null;
  idDomiciliario?: number | null;
  idFactura?: number | null;
}

export class DatosPedidoReembolso implements IDatosPedidoReembolso {
  constructor(
    public fechaPedido?: string | null,
    public valorFactura?: number | null,
    public pedidoDireccion?: string | null,
    public domiciliario?: string | null,
    public idPedido?: number | null,
    public idDomiciliario?: number | null,
    public idFactura?: number | null
  ) {}
}
