import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICategoriaProducto, CategoriaProducto } from '../categoria-producto.model';
import { CategoriaProductoService } from '../service/categoria-producto.service';

import { CategoriaProductoRoutingResolveService } from './categoria-producto-routing-resolve.service';

describe('CategoriaProducto routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CategoriaProductoRoutingResolveService;
  let service: CategoriaProductoService;
  let resultCategoriaProducto: ICategoriaProducto | undefined;

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
    routingResolveService = TestBed.inject(CategoriaProductoRoutingResolveService);
    service = TestBed.inject(CategoriaProductoService);
    resultCategoriaProducto = undefined;
  });

  describe('resolve', () => {
    it('should return ICategoriaProducto returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCategoriaProducto = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCategoriaProducto).toEqual({ id: 123 });
    });

    it('should return new ICategoriaProducto if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCategoriaProducto = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCategoriaProducto).toEqual(new CategoriaProducto());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CategoriaProducto })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCategoriaProducto = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCategoriaProducto).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
