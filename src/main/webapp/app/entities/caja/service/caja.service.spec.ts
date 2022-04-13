import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICaja, Caja } from '../caja.model';

import { CajaService } from './caja.service';

describe('Caja Service', () => {
  let service: CajaService;
  let httpMock: HttpTestingController;
  let elemDefault: ICaja;
  let expectedResult: ICaja | ICaja[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CajaService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      fechaCreacion: currentDate,
      valorTotalDia: 0,
      valorRegistradoDia: 0,
      diferencia: 0,
      estado: 'AAAAAAA',
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

    it('should create a Caja', () => {
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

      service.create(new Caja()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Caja', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          valorTotalDia: 1,
          valorRegistradoDia: 1,
          diferencia: 1,
          estado: 'BBBBBB',
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

    it('should partial update a Caja', () => {
      const patchObject = Object.assign(
        {
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          valorTotalDia: 1,
          valorRegistradoDia: 1,
          diferencia: 1,
        },
        new Caja()
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

    it('should return a list of Caja', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fechaCreacion: currentDate.format(DATE_TIME_FORMAT),
          valorTotalDia: 1,
          valorRegistradoDia: 1,
          diferencia: 1,
          estado: 'BBBBBB',
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

    it('should delete a Caja', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCajaToCollectionIfMissing', () => {
      it('should add a Caja to an empty array', () => {
        const caja: ICaja = { id: 123 };
        expectedResult = service.addCajaToCollectionIfMissing([], caja);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(caja);
      });

      it('should not add a Caja to an array that contains it', () => {
        const caja: ICaja = { id: 123 };
        const cajaCollection: ICaja[] = [
          {
            ...caja,
          },
          { id: 456 },
        ];
        expectedResult = service.addCajaToCollectionIfMissing(cajaCollection, caja);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Caja to an array that doesn't contain it", () => {
        const caja: ICaja = { id: 123 };
        const cajaCollection: ICaja[] = [{ id: 456 }];
        expectedResult = service.addCajaToCollectionIfMissing(cajaCollection, caja);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(caja);
      });

      it('should add only unique Caja to an array', () => {
        const cajaArray: ICaja[] = [{ id: 123 }, { id: 456 }, { id: 67287 }];
        const cajaCollection: ICaja[] = [{ id: 123 }];
        expectedResult = service.addCajaToCollectionIfMissing(cajaCollection, ...cajaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const caja: ICaja = { id: 123 };
        const caja2: ICaja = { id: 456 };
        expectedResult = service.addCajaToCollectionIfMissing([], caja, caja2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(caja);
        expect(expectedResult).toContain(caja2);
      });

      it('should accept null and undefined values', () => {
        const caja: ICaja = { id: 123 };
        expectedResult = service.addCajaToCollectionIfMissing([], null, caja, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(caja);
      });

      it('should return initial array if no Caja is added', () => {
        const cajaCollection: ICaja[] = [{ id: 123 }];
        expectedResult = service.addCajaToCollectionIfMissing(cajaCollection, undefined, null);
        expect(expectedResult).toEqual(cajaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
