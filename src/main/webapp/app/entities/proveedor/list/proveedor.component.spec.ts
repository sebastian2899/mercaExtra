import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ProveedorService } from '../service/proveedor.service';

import { ProveedorComponent } from './proveedor.component';

describe('Proveedor Management Component', () => {
  let comp: ProveedorComponent;
  let fixture: ComponentFixture<ProveedorComponent>;
  let service: ProveedorService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ProveedorComponent],
    })
      .overrideTemplate(ProveedorComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProveedorComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ProveedorService);

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
    expect(comp.proveedors?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
