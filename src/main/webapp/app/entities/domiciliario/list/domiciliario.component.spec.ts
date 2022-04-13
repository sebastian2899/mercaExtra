import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DomiciliarioService } from '../service/domiciliario.service';

import { DomiciliarioComponent } from './domiciliario.component';

describe('Domiciliario Management Component', () => {
  let comp: DomiciliarioComponent;
  let fixture: ComponentFixture<DomiciliarioComponent>;
  let service: DomiciliarioService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DomiciliarioComponent],
    })
      .overrideTemplate(DomiciliarioComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DomiciliarioComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DomiciliarioService);

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
    expect(comp.domiciliarios?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
