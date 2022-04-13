import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CompraService } from '../service/compra.service';

import { CompraComponent } from './compra.component';

describe('Compra Management Component', () => {
  let comp: CompraComponent;
  let fixture: ComponentFixture<CompraComponent>;
  let service: CompraService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CompraComponent],
    })
      .overrideTemplate(CompraComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompraComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(CompraService);

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
    expect(comp.compras?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
