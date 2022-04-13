import dayjs from 'dayjs/esm';
import { TipoFactura } from 'app/entities/enumerations/tipo-factura.model';
import { MetodoPago } from 'app/entities/enumerations/metodo-pago.model';

export interface IFactura {
  id?: number;
  fechaCreacion?: dayjs.Dayjs | null;
  infoCiente?: string | null;
  numeroFactura?: string | null;
  tipoFactura?: TipoFactura | null;
  valorFactura?: number | null;
  valorPagado?: number | null;
  valorDeuda?: number | null;
  estadoFactura?: number | null;
  metodoPago?: MetodoPago | null;
  userName?: string | null;
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
    public estadoFactura?: number | null,
    public metodoPago?: MetodoPago | null,
    public userName?: string | null
  ) {}
}

export function getFacturaIdentifier(factura: IFactura): number | undefined {
  return factura.id;
}
