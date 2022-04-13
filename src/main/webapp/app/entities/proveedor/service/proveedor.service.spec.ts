import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TipoDoc } from 'app/entities/enumerations/tipo-doc.model';
import { TipoProveedor } from 'app/entities/enumerations/tipo-proveedor.model';
import { IProveedor, Proveedor } from '../proveedor.model';

import { ProveedorService } from './proveedor.service';

describe('Proveedor Service', () => {
  let service: ProveedorService;
  let httpMock: HttpTestingController;
  let elemDefault: IProveedor;
  let expectedResult: IProveedor | IProveedor[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProveedorService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombre: 'AAAAAAA',
      apellido: 'AAAAAAA',
      tipoCC: TipoDoc.CC,
      numeroCC: 'AAAAAAA',
      numCelular: 'AAAAAAA',
      email: 'AAAAAAA',
      tipoProovedor: TipoProveedor.NATURAL,
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

    it('should create a Proveedor', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Proveedor()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Proveedor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          apellido: 'BBBBBB',
          tipoCC: 'BBBBBB',
          numeroCC: 'BBBBBB',
          numCelular: 'BBBBBB',
          email: 'BBBBBB',
          tipoProovedor: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Proveedor', () => {
      const patchObject = Object.assign(
        {
          nombre: 'BBBBBB',
          apellido: 'BBBBBB',
          numeroCC: 'BBBBBB',
          numCelular: 'BBBBBB',
          tipoProovedor: 'BBBBBB',
        },
        new Proveedor()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Proveedor', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombre: 'BBBBBB',
          apellido: 'BBBBBB',
          tipoCC: 'BBBBBB',
          numeroCC: 'BBBBBB',
          numCelular: 'BBBBBB',
          email: 'BBBBBB',
          tipoProovedor: 'BBBBBB',
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

    it('should delete a Proveedor', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addProveedorToCollectionIfMissing', () => {
      it('should add a Proveedor to an empty array', () => {
        const proveedor: IProveedor = { id: 123 };
        expectedResult = service.addProveedorToCollectionIfMissing([], proveedor);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(proveedor);
      });

      it('should not add a Proveedor to an array that contains it', () => {
        const proveedor: IProveedor = { id: 123 };
        const proveedorCollection: IProveedor[] = [
          {
            ...proveedor,
          },
          { id: 456 },
        ];
        expectedResult = service.addProveedorToCollectionIfMissing(proveedorCollection, proveedor);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Proveedor to an array that doesn't contain it", () => {
        const proveedor: IProveedor = { id: 123 };
        const proveedorCollection: IProveedor[] = [{ id: 456 }];
        expectedResult = service.addProveedorToCollectionIfMissing(proveedorCollection, proveedor);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(proveedor);
      });

      it('should add only unique Proveedor to an array', () => {
        const proveedorArray: IProveedor[] = [{ id: 123 }, { id: 456 }, { id: 19176 }];
        const proveedorCollection: IProveedor[] = [{ id: 123 }];
        expectedResult = service.addProveedorToCollectionIfMissing(proveedorCollection, ...proveedorArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const proveedor: IProveedor = { id: 123 };
        const proveedor2: IProveedor = { id: 456 };
        expectedResult = service.addProveedorToCollectionIfMissing([], proveedor, proveedor2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(proveedor);
        expect(expectedResult).toContain(proveedor2);
      });

      it('should accept null and undefined values', () => {
        const proveedor: IProveedor = { id: 123 };
        expectedResult = service.addProveedorToCollectionIfMissing([], null, proveedor, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(proveedor);
      });

      it('should return initial array if no Proveedor is added', () => {
        const proveedorCollection: IProveedor[] = [{ id: 123 }];
        expectedResult = service.addProveedorToCollectionIfMissing(proveedorCollection, undefined, null);
        expect(expectedResult).toEqual(proveedorCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
