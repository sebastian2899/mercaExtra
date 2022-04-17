import dayjs from 'dayjs/esm';
import { TipoFactura } from 'app/entities/enumerations/tipo-factura.model';
import { MetodoPago } from 'app/entities/enumerations/metodo-pago.model';
import { IItemFacturaVenta } from '../item-factura-venta/item-factura-venta.model';

export interface IFactura {
  id?: number;
  fechaCreacion?: dayjs.Dayjs | null;
  infoCiente?: string | null;
  numeroFactura?: string | null;
  tipoFactura?: TipoFactura | null;
  valorFactura?: number | null;
  valorPagado?: number | null;
  valorDeuda?: number | null;
  estadoFactura?: string | null;
  metodoPago?: MetodoPago | null;
  userName?: string | null;
  itemsPorFactura?: IItemFacturaVenta[] | null;
}

export class Factura implements IFactura {
  constructor(
    public id?: number,
    public fechaCreacion?: dayjs.Dayjs | null,
    public infoCiente?: string | null,
    public numeroFactura?: string | null,
    public tipoFactura?: TipoFactura | null,
    public valorFactura?: number | null,
    public valorPagado?: number | null,
    public valorDeuda?: number | null,
    public estadoFactura?: string | null,
    public metodoPago?: MetodoPago | null,
    public userName?: string | null,
    public itemsPorFactura?: IItemFacturaVenta[] | null
  ) {}
}

export function getFacturaIdentifier(factura: IFactura): number | undefined {
  return factura.id;
}
