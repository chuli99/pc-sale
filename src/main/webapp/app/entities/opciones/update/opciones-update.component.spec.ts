import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OpcionesFormService } from './opciones-form.service';
import { OpcionesService } from '../service/opciones.service';
import { IOpciones } from '../opciones.model';
import { IPersonalizaciones } from 'app/entities/personalizaciones/personalizaciones.model';
import { PersonalizacionesService } from 'app/entities/personalizaciones/service/personalizaciones.service';

import { OpcionesUpdateComponent } from './opciones-update.component';

describe('Opciones Management Update Component', () => {
  let comp: OpcionesUpdateComponent;
  let fixture: ComponentFixture<OpcionesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let opcionesFormService: OpcionesFormService;
  let opcionesService: OpcionesService;
  let personalizacionesService: PersonalizacionesService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [OpcionesUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(OpcionesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OpcionesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    opcionesFormService = TestBed.inject(OpcionesFormService);
    opcionesService = TestBed.inject(OpcionesService);
    personalizacionesService = TestBed.inject(PersonalizacionesService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Personalizaciones query and add missing value', () => {
      const opciones: IOpciones = { id: 456 };
      const personalizaciones: IPersonalizaciones = { id: 53988 };
      opciones.personalizaciones = personalizaciones;

      const personalizacionesCollection: IPersonalizaciones[] = [{ id: 31255 }];
      jest.spyOn(personalizacionesService, 'query').mockReturnValue(of(new HttpResponse({ body: personalizacionesCollection })));
      const additionalPersonalizaciones = [personalizaciones];
      const expectedCollection: IPersonalizaciones[] = [...additionalPersonalizaciones, ...personalizacionesCollection];
      jest.spyOn(personalizacionesService, 'addPersonalizacionesToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ opciones });
      comp.ngOnInit();

      expect(personalizacionesService.query).toHaveBeenCalled();
      expect(personalizacionesService.addPersonalizacionesToCollectionIfMissing).toHaveBeenCalledWith(
        personalizacionesCollection,
        ...additionalPersonalizaciones.map(expect.objectContaining)
      );
      expect(comp.personalizacionesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const opciones: IOpciones = { id: 456 };
      const personalizaciones: IPersonalizaciones = { id: 15384 };
      opciones.personalizaciones = personalizaciones;

      activatedRoute.data = of({ opciones });
      comp.ngOnInit();

      expect(comp.personalizacionesSharedCollection).toContain(personalizaciones);
      expect(comp.opciones).toEqual(opciones);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOpciones>>();
      const opciones = { id: 123 };
      jest.spyOn(opcionesFormService, 'getOpciones').mockReturnValue(opciones);
      jest.spyOn(opcionesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ opciones });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: opciones }));
      saveSubject.complete();

      // THEN
      expect(opcionesFormService.getOpciones).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(opcionesService.update).toHaveBeenCalledWith(expect.objectContaining(opciones));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOpciones>>();
      const opciones = { id: 123 };
      jest.spyOn(opcionesFormService, 'getOpciones').mockReturnValue({ id: null });
      jest.spyOn(opcionesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ opciones: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: opciones }));
      saveSubject.complete();

      // THEN
      expect(opcionesFormService.getOpciones).toHaveBeenCalled();
      expect(opcionesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOpciones>>();
      const opciones = { id: 123 };
      jest.spyOn(opcionesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ opciones });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(opcionesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePersonalizaciones', () => {
      it('Should forward to personalizacionesService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(personalizacionesService, 'comparePersonalizaciones');
        comp.comparePersonalizaciones(entity, entity2);
        expect(personalizacionesService.comparePersonalizaciones).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
