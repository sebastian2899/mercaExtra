import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IDomiciliario, Domiciliario } from '../domiciliario.model';
import { DomiciliarioService } from '../service/domiciliario.service';

import { DomiciliarioRoutingResolveService } from './domiciliario-routing-resolve.service';

describe('Domiciliario routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DomiciliarioRoutingResolveService;
  let service: DomiciliarioService;
  let resultDomiciliario: IDomiciliario | undefined;

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
    routingResolveService = TestBed.inject(DomiciliarioRoutingResolveService);
    service = TestBed.inject(DomiciliarioService);
    resultDomiciliario = undefined;
  });

  describe('resolve', () => {
    it('should return IDomiciliario returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDomiciliario = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDomiciliario).toEqual({ id: 123 });
    });

    it('should return new IDomiciliario if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDomiciliario = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDomiciliario).toEqual(new Domiciliario());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Domiciliario })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDomiciliario = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDomiciliario).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
