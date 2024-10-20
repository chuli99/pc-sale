import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AdicionalesFormService } from './adicionales-form.service';
import { AdicionalesService } from '../service/adicionales.service';
import { IAdicionales } from '../adicionales.model';
import { IDispositivos } from 'app/entities/dispositivos/dispositivos.model';
import { DispositivosService } from 'app/entities/dispositivos/service/dispositivos.service';

import { AdicionalesUpdateComponent } from './adicionales-update.component';

describe('Adicionales Management Update Component', () => {
  let comp: AdicionalesUpdateComponent;
  let fixture: ComponentFixture<AdicionalesUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let adicionalesFormService: AdicionalesFormService;
  let adicionalesService: AdicionalesService;
  let dispositivosService: DispositivosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AdicionalesUpdateComponent],
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
      .overrideTemplate(AdicionalesUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AdicionalesUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    adicionalesFormService = TestBed.inject(AdicionalesFormService);
    adicionalesService = TestBed.inject(AdicionalesService);
    dispositivosService = TestBed.inject(DispositivosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Dispositivos query and add missing value', () => {
      const adicionales: IAdicionales = { id: 456 };
      const dispositivos: IDispositivos = { id: 84648 };
      adicionales.dispositivos = dispositivos;

      const dispositivosCollection: IDispositivos[] = [{ id: 10038 }];
      jest.spyOn(dispositivosService, 'query').mockReturnValue(of(new HttpResponse({ body: dispositivosCollection })));
      const additionalDispositivos = [dispositivos];
      const expectedCollection: IDispositivos[] = [...additionalDispositivos, ...dispositivosCollection];
      jest.spyOn(dispositivosService, 'addDispositivosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ adicionales });
      comp.ngOnInit();

      expect(dispositivosService.query).toHaveBeenCalled();
      expect(dispositivosService.addDispositivosToCollectionIfMissing).toHaveBeenCalledWith(
        dispositivosCollection,
        ...additionalDispositivos.map(expect.objectContaining)
      );
      expect(comp.dispositivosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const adicionales: IAdicionales = { id: 456 };
      const dispositivos: IDispositivos = { id: 67914 };
      adicionales.dispositivos = dispositivos;

      activatedRoute.data = of({ adicionales });
      comp.ngOnInit();

      expect(comp.dispositivosSharedCollection).toContain(dispositivos);
      expect(comp.adicionales).toEqual(adicionales);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdicionales>>();
      const adicionales = { id: 123 };
      jest.spyOn(adicionalesFormService, 'getAdicionales').mockReturnValue(adicionales);
      jest.spyOn(adicionalesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ adicionales });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: adicionales }));
      saveSubject.complete();

      // THEN
      expect(adicionalesFormService.getAdicionales).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(adicionalesService.update).toHaveBeenCalledWith(expect.objectContaining(adicionales));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdicionales>>();
      const adicionales = { id: 123 };
      jest.spyOn(adicionalesFormService, 'getAdicionales').mockReturnValue({ id: null });
      jest.spyOn(adicionalesService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ adicionales: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: adicionales }));
      saveSubject.complete();

      // THEN
      expect(adicionalesFormService.getAdicionales).toHaveBeenCalled();
      expect(adicionalesService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAdicionales>>();
      const adicionales = { id: 123 };
      jest.spyOn(adicionalesService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ adicionales });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(adicionalesService.update).toHaveBeenCalled();
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
