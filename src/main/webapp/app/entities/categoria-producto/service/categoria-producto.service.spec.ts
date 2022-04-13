import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICategoriaProducto, CategoriaProducto } from '../categoria-producto.model';

import { CategoriaProductoService } from './categoria-producto.service';

describe('CategoriaProducto Service', () => {
  let service: CategoriaProductoService;
  let httpMock: HttpTestingController;
  let elemDefault: ICategoriaProducto;
  let expectedResult: ICategoriaProducto | ICategoriaProducto[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CategoriaProductoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nombreCategoria: 'AAAAAAA',
      descripcion: 'AAAAAAA',
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

    it('should create a CategoriaProducto', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CategoriaProducto()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CategoriaProducto', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombreCategoria: 'BBBBBB',
          descripcion: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CategoriaProducto', () => {
      const patchObject = Object.assign({}, new CategoriaProducto());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CategoriaProducto', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nombreCategoria: 'BBBBBB',
          descripcion: 'BBBBBB',
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

    it('should delete a CategoriaProducto', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCategoriaProductoToCollectionIfMissing', () => {
      it('should add a CategoriaProducto to an empty array', () => {
        const categoriaProducto: ICategoriaProducto = { id: 123 };
        expectedResult = service.addCategoriaProductoToCollectionIfMissing([], categoriaProducto);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(categoriaProducto);
      });

      it('should not add a CategoriaProducto to an array that contains it', () => {
        const categoriaProducto: ICategoriaProducto = { id: 123 };
        const categoriaProductoCollection: ICategoriaProducto[] = [
          {
            ...categoriaProducto,
          },
          { id: 456 },
        ];
        expectedResult = service.addCategoriaProductoToCollectionIfMissing(categoriaProductoCollection, categoriaProducto);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CategoriaProducto to an array that doesn't contain it", () => {
        const categoriaProducto: ICategoriaProducto = { id: 123 };
        const categoriaProductoCollection: ICategoriaProducto[] = [{ id: 456 }];
        expectedResult = service.addCategoriaProductoToCollectionIfMissing(categoriaProductoCollection, categoriaProducto);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(categoriaProducto);
      });

      it('should add only unique CategoriaProducto to an array', () => {
        const categoriaProductoArray: ICategoriaProducto[] = [{ id: 123 }, { id: 456 }, { id: 61984 }];
        const categoriaProductoCollection: ICategoriaProducto[] = [{ id: 123 }];
        expectedResult = service.addCategoriaProductoToCollectionIfMissing(categoriaProductoCollection, ...categoriaProductoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const categoriaProducto: ICategoriaProducto = { id: 123 };
        const categoriaProducto2: ICategoriaProducto = { id: 456 };
        expectedResult = service.addCategoriaProductoToCollectionIfMissing([], categoriaProducto, categoriaProducto2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(categoriaProducto);
        expect(expectedResult).toContain(categoriaProducto2);
      });

      it('should accept null and undefined values', () => {
        const categoriaProducto: ICategoriaProducto = { id: 123 };
        expectedResult = service.addCategoriaProductoToCollectionIfMissing([], null, categoriaProducto, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(categoriaProducto);
      });

      it('should return initial array if no CategoriaProducto is added', () => {
        const categoriaProductoCollection: ICategoriaProducto[] = [{ id: 123 }];
        expectedResult = service.addCategoriaProductoToCollectionIfMissing(categoriaProductoCollection, undefined, null);
        expect(expectedResult).toEqual(categoriaProductoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
