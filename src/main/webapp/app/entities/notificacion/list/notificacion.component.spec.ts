import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { NotificacionService } from '../service/notificacion.service';

import { NotificacionComponent } from './notificacion.component';

describe('Notificacion Management Component', () => {
  let comp: NotificacionComponent;
  let fixture: ComponentFixture<NotificacionComponent>;
  let service: NotificacionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [NotificacionComponent],
    })
      .overrideTemplate(NotificacionComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NotificacionComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(NotificacionService);

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
    expect(comp.notificacions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
