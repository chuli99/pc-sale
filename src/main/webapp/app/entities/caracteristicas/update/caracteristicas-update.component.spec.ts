import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CaracteristicasFormService } from './caracteristicas-form.service';
import { CaracteristicasService } from '../service/caracteristicas.service';
import { ICaracteristicas } from '../caracteristicas.model';
import { IDispositivos } from 'app/entities/dispositivos/dispositivos.model';
import { DispositivosService } from 'app/entities/dispositivos/service/dispositivos.service';

import { CaracteristicasUpdateComponent } from './caracteristicas-update.component';

describe('Caracteristicas Management Update Component', () => {
  let comp: CaracteristicasUpdateComponent;
  let fixture: ComponentFixture<CaracteristicasUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let caracteristicasFormService: CaracteristicasFormService;
  let caracteristicasService: CaracteristicasService;
  let dispositivosService: DispositivosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CaracteristicasUpdateComponent],
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
      .overrideTemplate(CaracteristicasUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CaracteristicasUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    caracteristicasFormService = TestBed.inject(CaracteristicasFormService);
    caracteristicasService = TestBed.inject(CaracteristicasService);
    dispositivosService = TestBed.inject(DispositivosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Dispositivos query and add missing value', () => {
      const caracteristicas: ICaracteristicas = { id: 456 };
      const dispositivos: IDispositivos = { id: 4141 };
      caracteristicas.dispositivos = dispositivos;

      const dispositivosCollection: IDispositivos[] = [{ id: 64035 }];
      jest.spyOn(dispositivosService, 'query').mockReturnValue(of(new HttpResponse({ body: dispositivosCollection })));
      const additionalDispositivos = [dispositivos];
      const expectedCollection: IDispositivos[] = [...additionalDispositivos, ...dispositivosCollection];
      jest.spyOn(dispositivosService, 'addDispositivosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ caracteristicas });
      comp.ngOnInit();

      expect(dispositivosService.query).toHaveBeenCalled();
      expect(dispositivosService.addDispositivosToCollectionIfMissing).toHaveBeenCalledWith(
        dispositivosCollection,
        ...additionalDispositivos.map(expect.objectContaining)
      );
      expect(comp.dispositivosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const caracteristicas: ICaracteristicas = { id: 456 };
      const dispositivos: IDispositivos = { id: 2207 };
      caracteristicas.dispositivos = dispositivos;

      activatedRoute.data = of({ caracteristicas });
      comp.ngOnInit();

      expect(comp.dispositivosSharedCollection).toContain(dispositivos);
      expect(comp.caracteristicas).toEqual(caracteristicas);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICaracteristicas>>();
      const caracteristicas = { id: 123 };
      jest.spyOn(caracteristicasFormService, 'getCaracteristicas').mockReturnValue(caracteristicas);
      jest.spyOn(caracteristicasService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ caracteristicas });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: caracteristicas }));
      saveSubject.complete();

      // THEN
      expect(caracteristicasFormService.getCaracteristicas).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(caracteristicasService.update).toHaveBeenCalledWith(expect.objectContaining(caracteristicas));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICaracteristicas>>();
      const caracteristicas = { id: 123 };
      jest.spyOn(caracteristicasFormService, 'getCaracteristicas').mockReturnValue({ id: null });
      jest.spyOn(caracteristicasService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ caracteristicas: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: caracteristicas }));
      saveSubject.complete();

      // THEN
      expect(caracteristicasFormService.getCaracteristicas).toHaveBeenCalled();
      expect(caracteristicasService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICaracteristicas>>();
      const caracteristicas = { id: 123 };
      jest.spyOn(caracteristicasService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ caracteristicas });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(caracteristicasService.update).toHaveBeenCalled();
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
