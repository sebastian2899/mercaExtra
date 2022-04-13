import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEgreso, Egreso } from '../egreso.model';

import { EgresoService } from './egreso.service';

describe('Egreso Service', () => {
  let service: EgresoService;
  let httpMock: HttpTestingController;
  let elemDefault: IEgreso;
  let expectedResult: IEgreso | IEgreso[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EgresoService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fechaCreacion: currentDate,
      descripcion: 'AAAAAAA',
      valorEgreso: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Egreso', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
        },
        returnedFromService
      );

      service.create(new Egreso()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Egreso', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          descripcion: 'BBBBBB',
          valorEgreso: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Egreso', () => {
      const patchObject = Object.assign(
        {
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          descripcion: 'BBBBBB',
        },
        new Egreso()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Egreso', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          descripcion: 'BBBBBB',
          valorEgreso: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fechaCreacion: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Egreso', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEgresoToCollectionIfMissing', () => {
      it('should add a Egreso to an empty array', () => {
        const egreso: IEgreso = { id: 123 };
        expectedResult = service.addEgresoToCollectionIfMissing([], egreso);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(egreso);
      });

      it('should not add a Egreso to an array that contains it', () => {
        const egreso: IEgreso = { id: 123 };
        const egresoCollection: IEgreso[] = [
          {
            ...egreso,
          },
          { id: 456 },
        ];
        expectedResult = service.addEgresoToCollectionIfMissing(egresoCollection, egreso);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Egreso to an array that doesn't contain it", () => {
        const egreso: IEgreso = { id: 123 };
        const egresoCollection: IEgreso[] = [{ id: 456 }];
        expectedResult = service.addEgresoToCollectionIfMissing(egresoCollection, egreso);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(egreso);
      });

      it('should add only unique Egreso to an array', () => {
        const egresoArray: IEgreso[] = [{ id: 123 }, { id: 456 }, { id: 52042 }];
        const egresoCollection: IEgreso[] = [{ id: 123 }];
        expectedResult = service.addEgresoToCollectionIfMissing(egresoCollection, ...egresoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const egreso: IEgreso = { id: 123 };
        const egreso2: IEgreso = { id: 456 };
        expectedResult = service.addEgresoToCollectionIfMissing([], egreso, egreso2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(egreso);
        expect(expectedResult).toContain(egreso2);
      });

      it('should accept null and undefined values', () => {
        const egreso: IEgreso = { id: 123 };
        expectedResult = service.addEgresoToCollectionIfMissing([], null, egreso, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(egreso);
      });

      it('should return initial array if no Egreso is added', () => {
        const egresoCollection: IEgreso[] = [{ id: 123 }];
        expectedResult = service.addEgresoToCollectionIfMissing(egresoCollection, undefined, null);
        expect(expectedResult).toEqual(egresoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
