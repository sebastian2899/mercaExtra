import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PedidoService } from '../service/pedido.service';

import { PedidoComponent } from './pedido.component';

describe('Pedido Management Component', () => {
  let comp: PedidoComponent;
  let fixture: ComponentFixture<PedidoComponent>;
  let service: PedidoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PedidoComponent],
    })
      .overrideTemplate(PedidoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PedidoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PedidoService);

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
    expect(comp.pedidos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
