import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ProductoService } from '../service/producto.service';

import { ProductoComponent } from './producto.component';

describe('Producto Management Component', () => {
  let comp: ProductoComponent;
  let fixture: ComponentFixture<ProductoComponent>;
  let service: ProductoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ProductoComponent],
    })
      .overrideTemplate(ProductoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProductoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ProductoService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.productos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
