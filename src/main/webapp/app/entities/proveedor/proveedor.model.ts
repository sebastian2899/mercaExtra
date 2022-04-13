import { TipoDoc } from 'app/entities/enumerations/tipo-doc.model';
import { TipoProveedor } from 'app/entities/enumerations/tipo-proveedor.model';

export interface IProveedor {
  id?: number;
  nombre?: string | null;
  apellido?: string | null;
  tipoCC?: TipoDoc | null;
  numeroCC?: string | null;
  numCelular?: string | null;
  email?: string | null;
  tipoProovedor?: TipoProveedor | null;
}

export class Proveedor implements IProveedor {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public apellido?: string | null,
    public tipoCC?: TipoDoc | null,
    public numeroCC?: string | null,
    public numCelular?: string | null,
    public email?: string | null,
    public tipoProovedor?: TipoProveedor | null
  ) {}
}

export function getProveedorIdentifier(proveedor: IProveedor): number | undefined {
  return proveedor.id;
}
