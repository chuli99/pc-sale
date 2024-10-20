import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonalizacionesDetailComponent } from './personalizaciones-detail.component';

describe('Personalizaciones Management Detail Component', () => {
  let comp: PersonalizacionesDetailComponent;
  let fixture: ComponentFixture<PersonalizacionesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PersonalizacionesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ personalizaciones: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PersonalizacionesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PersonalizacionesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load personalizaciones on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.personalizaciones).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
