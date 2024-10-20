import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AdicionalesDetailComponent } from './adicionales-detail.component';

describe('Adicionales Management Detail Component', () => {
  let comp: AdicionalesDetailComponent;
  let fixture: ComponentFixture<AdicionalesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdicionalesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ adicionales: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AdicionalesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AdicionalesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load adicionales on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.adicionales).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
