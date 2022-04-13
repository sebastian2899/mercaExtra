import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EgresoService } from '../service/egreso.service';

import { EgresoComponent } from './egreso.component';

describe('Egreso Management Component', () => {
  let comp: EgresoComponent;
  let fixture: ComponentFixture<EgresoComponent>;
  let service: EgresoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [EgresoComponent],
    })
      .overrideTemplate(EgresoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EgresoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(EgresoService);

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
    expect(comp.egresos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
