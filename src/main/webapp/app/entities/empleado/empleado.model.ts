import dayjs from 'dayjs/esm';

export interface IEmpleado {
  id?: number;
  nombre?: string | null;
  apellido?: string | null;
  email?: string | null;
  numCelular?: string | null;
  direccion?: dayjs.Dayjs | null;
  salario?: number | null;
  horario?: string | null;
}

export class Empleado implements IEmpleado {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public apellido?: string | null,
    public email?: string | null,
    public numCelular?: string | null,
    public direccion?: dayjs.Dayjs | null,
    public salario?: number | null,
    public horario?: string | null
  ) {}
}

export function getEmpleadoIdentifier(empleado: IEmpleado): number | undefined {
  return empleado.id;
}
