import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReembolso, Reembolso } from '../reembolso.model';

import { ReembolsoService } from './reembolso.service';

describe('Reembolso Service', () => {
  let service: ReembolsoService;
  let httpMock: HttpTestingController;
  let elemDefault: IReembolso;
  let expectedResult: IReembolso | IReembolso[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ReembolsoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      idPedido: 0,
      idDomiciliario: 0,
      idFactura: 0,
      descripcion: 'AAAAAAA',
      estado: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Reembolso', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Reembolso()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Reembolso', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idPedido: 1,
          idDomiciliario: 1,
          idFactura: 1,
          descripcion: 'BBBBBB',
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Reembolso', () => {
      const patchObject = Object.assign(
        {
          idPedido: 1,
          idFactura: 1,
          estado: 'BBBBBB',
        },
        new Reembolso()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Reembolso', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idPedido: 1,
          idDomiciliario: 1,
          idFactura: 1,
          descripcion: 'BBBBBB',
          estado: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Reembolso', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addReembolsoToCollectionIfMissing', () => {
      it('should add a Reembolso to an empty array', () => {
        const reembolso: IReembolso = { id: 123 };
        expectedResult = service.addReembolsoToCollectionIfMissing([], reembolso);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reembolso);
      });

      it('should not add a Reembolso to an array that contains it', () => {
        const reembolso: IReembolso = { id: 123 };
        const reembolsoCollection: IReembolso[] = [
          {
            ...reembolso,
          },
          { id: 456 },
        ];
        expectedResult = service.addReembolsoToCollectionIfMissing(reembolsoCollection, reembolso);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Reembolso to an array that doesn't contain it", () => {
        const reembolso: IReembolso = { id: 123 };
        const reembolsoCollection: IReembolso[] = [{ id: 456 }];
        expectedResult = service.addReembolsoToCollectionIfMissing(reembolsoCollection, reembolso);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reembolso);
      });

      it('should add only unique Reembolso to an array', () => {
        const reembolsoArray: IReembolso[] = [{ id: 123 }, { id: 456 }, { id: 54622 }];
        const reembolsoCollection: IReembolso[] = [{ id: 123 }];
        expectedResult = service.addReembolsoToCollectionIfMissing(reembolsoCollection, ...reembolsoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const reembolso: IReembolso = { id: 123 };
        const reembolso2: IReembolso = { id: 456 };
        expectedResult = service.addReembolsoToCollectionIfMissing([], reembolso, reembolso2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(reembolso);
        expect(expectedResult).toContain(reembolso2);
      });

      it('should accept null and undefined values', () => {
        const reembolso: IReembolso = { id: 123 };
        expectedResult = service.addReembolsoToCollectionIfMissing([], null, reembolso, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(reembolso);
      });

      it('should return initial array if no Reembolso is added', () => {
        const reembolsoCollection: IReembolso[] = [{ id: 123 }];
        expectedResult = service.addReembolsoToCollectionIfMissing(reembolsoCollection, undefined, null);
        expect(expectedResult).toEqual(reembolsoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
