import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { INotificacion, Notificacion } from '../notificacion.model';

import { NotificacionService } from './notificacion.service';

describe('Notificacion Service', () => {
  let service: NotificacionService;
  let httpMock: HttpTestingController;
  let elemDefault: INotificacion;
  let expectedResult: INotificacion | INotificacion[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NotificacionService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      descripcion: 'AAAAAAA',
      fecha: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Notificacion', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.create(new Notificacion()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Notificacion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          descripcion: 'BBBBBB',
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Notificacion', () => {
      const patchObject = Object.assign(
        {
          descripcion: 'BBBBBB',
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        new Notificacion()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Notificacion', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          descripcion: 'BBBBBB',
          fecha: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fecha: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Notificacion', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addNotificacionToCollectionIfMissing', () => {
      it('should add a Notificacion to an empty array', () => {
        const notificacion: INotificacion = { id: 123 };
        expectedResult = service.addNotificacionToCollectionIfMissing([], notificacion);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(notificacion);
      });

      it('should not add a Notificacion to an array that contains it', () => {
        const notificacion: INotificacion = { id: 123 };
        const notificacionCollection: INotificacion[] = [
          {
            ...notificacion,
          },
          { id: 456 },
        ];
        expectedResult = service.addNotificacionToCollectionIfMissing(notificacionCollection, notificacion);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Notificacion to an array that doesn't contain it", () => {
        const notificacion: INotificacion = { id: 123 };
        const notificacionCollection: INotificacion[] = [{ id: 456 }];
        expectedResult = service.addNotificacionToCollectionIfMissing(notificacionCollection, notificacion);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notificacion);
      });

      it('should add only unique Notificacion to an array', () => {
        const notificacionArray: INotificacion[] = [{ id: 123 }, { id: 456 }, { id: 87971 }];
        const notificacionCollection: INotificacion[] = [{ id: 123 }];
        expectedResult = service.addNotificacionToCollectionIfMissing(notificacionCollection, ...notificacionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const notificacion: INotificacion = { id: 123 };
        const notificacion2: INotificacion = { id: 456 };
        expectedResult = service.addNotificacionToCollectionIfMissing([], notificacion, notificacion2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(notificacion);
        expect(expectedResult).toContain(notificacion2);
      });

      it('should accept null and undefined values', () => {
        const notificacion: INotificacion = { id: 123 };
        expectedResult = service.addNotificacionToCollectionIfMissing([], null, notificacion, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(notificacion);
      });

      it('should return initial array if no Notificacion is added', () => {
        const notificacionCollection: INotificacion[] = [{ id: 123 }];
        expectedResult = service.addNotificacionToCollectionIfMissing(notificacionCollection, undefined, null);
        expect(expectedResult).toEqual(notificacionCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
