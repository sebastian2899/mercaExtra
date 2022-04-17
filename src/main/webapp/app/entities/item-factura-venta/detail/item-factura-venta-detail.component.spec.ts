import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ItemFacturaVentaDetailComponent } from './item-factura-venta-detail.component';

describe('ItemFacturaVenta Management Detail Component', () => {
  let comp: ItemFacturaVentaDetailComponent;
  let fixture: ComponentFixture<ItemFacturaVentaDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ItemFacturaVentaDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ itemFacturaVenta: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ItemFacturaVentaDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ItemFacturaVentaDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load itemFacturaVenta on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.itemFacturaVenta).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
