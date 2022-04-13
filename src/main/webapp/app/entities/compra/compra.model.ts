import { TipoFactura } from 'app/entities/enumerations/tipo-factura.model';

export interface ICompra {
  id?: number;
  fechaCreacion?: number | null;
  numeroFactura?: string | null;
  tipoFactura?: TipoFactura | null;
  informacionProovedor?: string | null;
  idProveedor?: number | null;
  valorFactura?: number | null;
  valorPagado?: number | null;
  valorDeuda?: number | null;
  estado?: string | null;
}

export class Compra implements ICompra {
  constructor(
    public id?: number,
    public fechaCreacion?: number | null,
    public numeroFactura?: string | null,
    public tipoFactura?: TipoFactura | null,
    public informacionProovedor?: string | null,
    public idProveedor?: number | null,
    public valorFactura?: number | null,
    public valorPagado?: number | null,
    public valorDeuda?: number | null,
    public estado?: string | null
  ) {}
}

export function getCompraIdentifier(compra: ICompra): number | undefined {
  return compra.id;
}
