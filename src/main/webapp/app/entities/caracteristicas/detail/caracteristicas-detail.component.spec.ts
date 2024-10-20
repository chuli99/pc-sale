import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CaracteristicasDetailComponent } from './caracteristicas-detail.component';

describe('Caracteristicas Management Detail Component', () => {
  let comp: CaracteristicasDetailComponent;
  let fixture: ComponentFixture<CaracteristicasDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CaracteristicasDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ caracteristicas: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CaracteristicasDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CaracteristicasDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load caracteristicas on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.caracteristicas).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
