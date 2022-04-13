import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CategoriaProductoService } from '../service/categoria-producto.service';

import { CategoriaProductoComponent } from './categoria-producto.component';

describe('CategoriaProducto Management Component', () => {
  let comp: CategoriaProductoComponent;
  let fixture: ComponentFixture<CategoriaProductoComponent>;
  let service: CategoriaProductoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CategoriaProductoComponent],
    })
      .overrideTemplate(CategoriaProductoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CategoriaProductoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CategoriaProductoService);

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
    expect(comp.categoriaProductos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
