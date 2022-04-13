import { TipoSalario } from 'app/entities/enumerations/tipo-salario.model';
import { EstadoDomiciliario } from 'app/entities/enumerations/estado-domiciliario.model';

export interface IDomiciliario {
  id?: number;
  nombre?: string | null;
  apellido?: string | null;
  salario?: TipoSalario | null;
  telefono?: string | null;
  horario?: string | null;
  sueldo?: number | null;
  estado?: EstadoDomiciliario | null;
}

export class Domiciliario implements IDomiciliario {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public apellido?: string | null,
    public salario?: TipoSalario | null,
    public telefono?: string | null,
    public horario?: string | null,
    public sueldo?: number | null,
    public estado?: EstadoDomiciliario | null
  ) {}
}

export function getDomiciliarioIdentifier(domiciliario: IDomiciliario): number | undefined {
  return domiciliario.id;
}
