import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CajaService } from '../service/caja.service';

import { CajaComponent } from './caja.component';

describe('Caja Management Component', () => {
  let comp: CajaComponent;
  let fixture: ComponentFixture<CajaComponent>;
  let service: CajaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CajaComponent],
    })
      .overrideTemplate(CajaComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CajaComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CajaService);

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
    expect(comp.cajas?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
