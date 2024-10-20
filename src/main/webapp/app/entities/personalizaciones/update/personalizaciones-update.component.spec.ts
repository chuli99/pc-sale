import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PersonalizacionesFormService } from './personalizaciones-form.service';
import { PersonalizacionesService } from '../service/personalizaciones.service';
import { IPersonalizaciones } from '../personalizaciones.model';
import { IDispositivos } from 'app/entities/dispositivos/dispositivos.model';
import { DispositivosService } from 'app/entities/dispositivos/service/dispositivos.service';

import { PersonalizacionesUpdateComponent } from './personalizaciones-update.component';

describe('Personalizaciones Management Update Component', () => {
  let comp: PersonalizacionesUpdateComponent;
  let fixture: ComponentFixture<PersonalizacionesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let personalizacionesFormService: PersonalizacionesFormService;
  let personalizacionesService: PersonalizacionesService;
  let dispositivosService: DispositivosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PersonalizacionesUpdateComponent],
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
      .overrideTemplate(PersonalizacionesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PersonalizacionesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    personalizacionesFormService = TestBed.inject(PersonalizacionesFormService);
    personalizacionesService = TestBed.inject(PersonalizacionesService);
    dispositivosService = TestBed.inject(DispositivosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Dispositivos query and add missing value', () => {
      const personalizaciones: IPersonalizaciones = { id: 456 };
      const dispositivos: IDispositivos = { id: 52838 };
      personalizaciones.dispositivos = dispositivos;

      const dispositivosCollection: IDispositivos[] = [{ id: 39361 }];
      jest.spyOn(dispositivosService, 'query').mockReturnValue(of(new HttpResponse({ body: dispositivosCollection })));
      const additionalDispositivos = [dispositivos];
      const expectedCollection: IDispositivos[] = [...additionalDispositivos, ...dispositivosCollection];
      jest.spyOn(dispositivosService, 'addDispositivosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ personalizaciones });
      comp.ngOnInit();

      expect(dispositivosService.query).toHaveBeenCalled();
      expect(dispositivosService.addDispositivosToCollectionIfMissing).toHaveBeenCalledWith(
        dispositivosCollection,
        ...additionalDispositivos.map(expect.objectContaining)
      );
      expect(comp.dispositivosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const personalizaciones: IPersonalizaciones = { id: 456 };
      const dispositivos: IDispositivos = { id: 25076 };
      personalizaciones.dispositivos = dispositivos;

      activatedRoute.data = of({ personalizaciones });
      comp.ngOnInit();

      expect(comp.dispositivosSharedCollection).toContain(dispositivos);
      expect(comp.personalizaciones).toEqual(personalizaciones);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersonalizaciones>>();
      const personalizaciones = { id: 123 };
      jest.spyOn(personalizacionesFormService, 'getPersonalizaciones').mockReturnValue(personalizaciones);
      jest.spyOn(personalizacionesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personalizaciones });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personalizaciones }));
      saveSubject.complete();

      // THEN
      expect(personalizacionesFormService.getPersonalizaciones).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(personalizacionesService.update).toHaveBeenCalledWith(expect.objectContaining(personalizaciones));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersonalizaciones>>();
      const personalizaciones = { id: 123 };
      jest.spyOn(personalizacionesFormService, 'getPersonalizaciones').mockReturnValue({ id: null });
      jest.spyOn(personalizacionesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personalizaciones: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personalizaciones }));
      saveSubject.complete();

      // THEN
      expect(personalizacionesFormService.getPersonalizaciones).toHaveBeenCalled();
      expect(personalizacionesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersonalizaciones>>();
      const personalizaciones = { id: 123 };
      jest.spyOn(personalizacionesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personalizaciones });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(personalizacionesService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDispositivos', () => {
      it('Should forward to dispositivosService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(dispositivosService, 'compareDispositivos');
        comp.compareDispositivos(entity, entity2);
        expect(dispositivosService.compareDispositivos).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
