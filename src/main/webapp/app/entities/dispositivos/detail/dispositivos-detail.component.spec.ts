import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DispositivosDetailComponent } from './dispositivos-detail.component';

describe('Dispositivos Management Detail Component', () => {
  let comp: DispositivosDetailComponent;
  let fixture: ComponentFixture<DispositivosDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DispositivosDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dispositivos: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DispositivosDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DispositivosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dispositivos on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dispositivos).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
