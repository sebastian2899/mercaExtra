import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IItemFacturaVenta, ItemFacturaVenta } from '../item-factura-venta.model';
import { ItemFacturaVentaService } from '../service/item-factura-venta.service';

import { ItemFacturaVentaRoutingResolveService } from './item-factura-venta-routing-resolve.service';

describe('ItemFacturaVenta routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ItemFacturaVentaRoutingResolveService;
  let service: ItemFacturaVentaService;
  let resultItemFacturaVenta: IItemFacturaVenta | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(ItemFacturaVentaRoutingResolveService);
    service = TestBed.inject(ItemFacturaVentaService);
    resultItemFacturaVenta = undefined;
  });

  describe('resolve', () => {
    it('should return IItemFacturaVenta returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultItemFacturaVenta = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultItemFacturaVenta).toEqual({ id: 123 });
    });

    it('should return new IItemFacturaVenta if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultItemFacturaVenta = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultItemFacturaVenta).toEqual(new ItemFacturaVenta());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ItemFacturaVenta })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultItemFacturaVenta = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultItemFacturaVenta).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
