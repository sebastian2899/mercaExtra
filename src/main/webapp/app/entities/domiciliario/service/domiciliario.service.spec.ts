import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TipoSalario } from 'app/entities/enumerations/tipo-salario.model';
import { EstadoDomiciliario } from 'app/entities/enumerations/estado-domiciliario.model';
import { IDomiciliario, Domiciliario } from '../domiciliario.model';

import { DomiciliarioService } from './domiciliario.service';

describe('Domiciliario Service', () => {
  let service: DomiciliarioService;
  let httpMock: HttpTestingController;
  let elemDefault: IDomiciliario;
  let expectedResult: IDomiciliario | IDomiciliario[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DomiciliarioService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      apellido: 'AAAAAAA',
      salario: TipoSalario.SALARIO_NORMAL,
      telefono: 'AAAAAAA',
      horario: 'AAAAAAA',
      sueldo: 0,
      estado: EstadoDomiciliario.EN_ENTREGA,
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

    it('should create a Domiciliario', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Domiciliario()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Domiciliario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          apellido: 'BBBBBB',
          salario: 'BBBBBB',
          telefono: 'BBBBBB',
          horario: 'BBBBBB',
          sueldo: 1,
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

    it('should partial update a Domiciliario', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
          apellido: 'BBBBBB',
          sueldo: 1,
          estado: 'BBBBBB',
        },
        new Domiciliario()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Domiciliario', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          apellido: 'BBBBBB',
          salario: 'BBBBBB',
          telefono: 'BBBBBB',
          horario: 'BBBBBB',
          sueldo: 1,
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

    it('should delete a Domiciliario', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDomiciliarioToCollectionIfMissing', () => {
      it('should add a Domiciliario to an empty array', () => {
        const domiciliario: IDomiciliario = { id: 123 };
        expectedResult = service.addDomiciliarioToCollectionIfMissing([], domiciliario);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(domiciliario);
      });

      it('should not add a Domiciliario to an array that contains it', () => {
        const domiciliario: IDomiciliario = { id: 123 };
        const domiciliarioCollection: IDomiciliario[] = [
          {
            ...domiciliario,
          },
          { id: 456 },
        ];
        expectedResult = service.addDomiciliarioToCollectionIfMissing(domiciliarioCollection, domiciliario);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Domiciliario to an array that doesn't contain it", () => {
        const domiciliario: IDomiciliario = { id: 123 };
        const domiciliarioCollection: IDomiciliario[] = [{ id: 456 }];
        expectedResult = service.addDomiciliarioToCollectionIfMissing(domiciliarioCollection, domiciliario);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(domiciliario);
      });

      it('should add only unique Domiciliario to an array', () => {
        const domiciliarioArray: IDomiciliario[] = [{ id: 123 }, { id: 456 }, { id: 40337 }];
        const domiciliarioCollection: IDomiciliario[] = [{ id: 123 }];
        expectedResult = service.addDomiciliarioToCollectionIfMissing(domiciliarioCollection, ...domiciliarioArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const domiciliario: IDomiciliario = { id: 123 };
        const domiciliario2: IDomiciliario = { id: 456 };
        expectedResult = service.addDomiciliarioToCollectionIfMissing([], domiciliario, domiciliario2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(domiciliario);
        expect(expectedResult).toContain(domiciliario2);
      });

      it('should accept null and undefined values', () => {
        const domiciliario: IDomiciliario = { id: 123 };
        expectedResult = service.addDomiciliarioToCollectionIfMissing([], null, domiciliario, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(domiciliario);
      });

      it('should return initial array if no Domiciliario is added', () => {
        const domiciliarioCollection: IDomiciliario[] = [{ id: 123 }];
        expectedResult = service.addDomiciliarioToCollectionIfMissing(domiciliarioCollection, undefined, null);
        expect(expectedResult).toEqual(domiciliarioCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
