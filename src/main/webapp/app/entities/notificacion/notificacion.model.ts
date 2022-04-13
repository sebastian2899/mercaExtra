import dayjs from 'dayjs/esm';

export interface INotificacion {
  id?: number;
  descripcion?: string | null;
  fecha?: dayjs.Dayjs | null;
}

export class Notificacion implements INotificacion {
  constructor(public id?: number, public descripcion?: string | null, public fecha?: dayjs.Dayjs | null) {}
}

export function getNotificacionIdentifier(notificacion: INotificacion): number | undefined {
  return notificacion.id;
}
