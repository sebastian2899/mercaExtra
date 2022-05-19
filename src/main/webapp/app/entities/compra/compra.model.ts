import { TipoFactura } from 'app/entities/enumerations/tipo-factura.model';
import dayjs from 'dayjs/esm';
import { IItemFacturaCompra } from './ItemFacturaCompra';

export interface ICompra {
  id?: number;
  fechaCreacion?: dayjs.Dayjs | null;
  numeroFactura?: string | null;
  tipoFactura?: TipoFactura | null;
  informacionProovedor?: string | null;
  idProveedor?: number | null;
  valorFactura?: number | null;
  valorPagado?: number | null;
  itemsFacturaCompra?: IItemFacturaCompra[] | null;
  valorDeuda?: number | null;
  estado?: string | null;
}

export class Compra implements ICompra {
  constructor(
    public id?: number,
    public fechaCreacion?: dayjs.Dayjs | null,
    public numeroFactura?: string | null,
    public tipoFactura?: TipoFactura | null,
    public informacionProovedor?: string | null,
    public idProveedor?: number | null,
    public valorFactura?: number | null,
    public valorPagado?: number | null,
    public valorDeuda?: number | null,
    public estado?: string | null,
    public itemsFacturaCompra?: IItemFacturaCompra[] | null
  ) {}
}

export function getCompraIdentifier(compra: ICompra): number | undefined {
  return compra.id;
}
