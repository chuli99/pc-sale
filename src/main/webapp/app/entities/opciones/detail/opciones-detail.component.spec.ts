import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OpcionesDetailComponent } from './opciones-detail.component';

describe('Opciones Management Detail Component', () => {
  let comp: OpcionesDetailComponent;
  let fixture: ComponentFixture<OpcionesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [OpcionesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ opciones: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(OpcionesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OpcionesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load opciones on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.opciones).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
