import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IItemFacturaVenta, ItemFacturaVenta } from '../item-factura-venta.model';

import { ItemFacturaVentaService } from './item-factura-venta.service';

describe('ItemFacturaVenta Service', () => {
  let service: ItemFacturaVentaService;
  let httpMock: HttpTestingController;
  let elemDefault: IItemFacturaVenta;
  let expectedResult: IItemFacturaVenta | IItemFacturaVenta[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ItemFacturaVentaService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      idFactura: 0,
      idProducto: 0,
      cantidad: 0,
      precio: 0,
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

    it('should create a ItemFacturaVenta', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ItemFacturaVenta()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ItemFacturaVenta', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idFactura: 1,
          idProducto: 1,
          cantidad: 1,
          precio: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ItemFacturaVenta', () => {
      const patchObject = Object.assign(
        {
          idProducto: 1,
        },
        new ItemFacturaVenta()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ItemFacturaVenta', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          idFactura: 1,
          idProducto: 1,
          cantidad: 1,
          precio: 1,
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

    it('should delete a ItemFacturaVenta', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addItemFacturaVentaToCollectionIfMissing', () => {
      it('should add a ItemFacturaVenta to an empty array', () => {
        const itemFacturaVenta: IItemFacturaVenta = { id: 123 };
        expectedResult = service.addItemFacturaVentaToCollectionIfMissing([], itemFacturaVenta);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(itemFacturaVenta);
      });

      it('should not add a ItemFacturaVenta to an array that contains it', () => {
        const itemFacturaVenta: IItemFacturaVenta = { id: 123 };
        const itemFacturaVentaCollection: IItemFacturaVenta[] = [
          {
            ...itemFacturaVenta,
          },
          { id: 456 },
        ];
        expectedResult = service.addItemFacturaVentaToCollectionIfMissing(itemFacturaVentaCollection, itemFacturaVenta);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ItemFacturaVenta to an array that doesn't contain it", () => {
        const itemFacturaVenta: IItemFacturaVenta = { id: 123 };
        const itemFacturaVentaCollection: IItemFacturaVenta[] = [{ id: 456 }];
        expectedResult = service.addItemFacturaVentaToCollectionIfMissing(itemFacturaVentaCollection, itemFacturaVenta);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(itemFacturaVenta);
      });

      it('should add only unique ItemFacturaVenta to an array', () => {
        const itemFacturaVentaArray: IItemFacturaVenta[] = [{ id: 123 }, { id: 456 }, { id: 85914 }];
        const itemFacturaVentaCollection: IItemFacturaVenta[] = [{ id: 123 }];
        expectedResult = service.addItemFacturaVentaToCollectionIfMissing(itemFacturaVentaCollection, ...itemFacturaVentaArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const itemFacturaVenta: IItemFacturaVenta = { id: 123 };
        const itemFacturaVenta2: IItemFacturaVenta = { id: 456 };
        expectedResult = service.addItemFacturaVentaToCollectionIfMissing([], itemFacturaVenta, itemFacturaVenta2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(itemFacturaVenta);
        expect(expectedResult).toContain(itemFacturaVenta2);
      });

      it('should accept null and undefined values', () => {
        const itemFacturaVenta: IItemFacturaVenta = { id: 123 };
        expectedResult = service.addItemFacturaVentaToCollectionIfMissing([], null, itemFacturaVenta, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(itemFacturaVenta);
      });

      it('should return initial array if no ItemFacturaVenta is added', () => {
        const itemFacturaVentaCollection: IItemFacturaVenta[] = [{ id: 123 }];
        expectedResult = service.addItemFacturaVentaToCollectionIfMissing(itemFacturaVentaCollection, undefined, null);
        expect(expectedResult).toEqual(itemFacturaVentaCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
