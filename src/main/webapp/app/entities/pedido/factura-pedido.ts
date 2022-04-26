export interface IFacturaPedido {
  idFactura?: number | null;
  infoCliente?: string | null;
  numeroFactura?: string | null;
  valorFactura?: number | null;
  estadoFactura?: string | null;
}

export class FacturaPedido implements IFacturaPedido {
  constructor(
    public idFactura?: number | null,
    public infoCliente?: string | null,
    public numeroFactura?: string | null,
    public valorFacutra?: number | null,
    public estadoFactura?: string | null
  ) {}
}
