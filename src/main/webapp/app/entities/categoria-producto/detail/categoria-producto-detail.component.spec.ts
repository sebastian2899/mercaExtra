import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CategoriaProductoDetailComponent } from './categoria-producto-detail.component';

describe('CategoriaProducto Management Detail Component', () => {
  let comp: CategoriaProductoDetailComponent;
  let fixture: ComponentFixture<CategoriaProductoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CategoriaProductoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ categoriaProducto: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CategoriaProductoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CategoriaProductoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load categoriaProducto on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.categoriaProducto).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
