import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { VentasFormService } from './ventas-form.service';
import { VentasService } from '../service/ventas.service';
import { IVentas } from '../ventas.model';
import { IDispositivos } from 'app/entities/dispositivos/dispositivos.model';
import { DispositivosService } from 'app/entities/dispositivos/service/dispositivos.service';

import { VentasUpdateComponent } from './ventas-update.component';

describe('Ventas Management Update Component', () => {
  let comp: VentasUpdateComponent;
  let fixture: ComponentFixture<VentasUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ventasFormService: VentasFormService;
  let ventasService: VentasService;
  let dispositivosService: DispositivosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [VentasUpdateComponent],
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
      .overrideTemplate(VentasUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VentasUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ventasFormService = TestBed.inject(VentasFormService);
    ventasService = TestBed.inject(VentasService);
    dispositivosService = TestBed.inject(DispositivosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Dispositivos query and add missing value', () => {
      const ventas: IVentas = { id: 456 };
      const idDispositivo: IDispositivos = { id: 34885 };
      ventas.idDispositivo = idDispositivo;

      const dispositivosCollection: IDispositivos[] = [{ id: 19328 }];
      jest.spyOn(dispositivosService, 'query').mockReturnValue(of(new HttpResponse({ body: dispositivosCollection })));
      const additionalDispositivos = [idDispositivo];
      const expectedCollection: IDispositivos[] = [...additionalDispositivos, ...dispositivosCollection];
      jest.spyOn(dispositivosService, 'addDispositivosToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ ventas });
      comp.ngOnInit();

      expect(dispositivosService.query).toHaveBeenCalled();
      expect(dispositivosService.addDispositivosToCollectionIfMissing).toHaveBeenCalledWith(
        dispositivosCollection,
        ...additionalDispositivos.map(expect.objectContaining)
      );
      expect(comp.dispositivosSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const ventas: IVentas = { id: 456 };
      const idDispositivo: IDispositivos = { id: 58718 };
      ventas.idDispositivo = idDispositivo;

      activatedRoute.data = of({ ventas });
      comp.ngOnInit();

      expect(comp.dispositivosSharedCollection).toContain(idDispositivo);
      expect(comp.ventas).toEqual(ventas);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVentas>>();
      const ventas = { id: 123 };
      jest.spyOn(ventasFormService, 'getVentas').mockReturnValue(ventas);
      jest.spyOn(ventasService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ventas });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ventas }));
      saveSubject.complete();

      // THEN
      expect(ventasFormService.getVentas).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ventasService.update).toHaveBeenCalledWith(expect.objectContaining(ventas));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVentas>>();
      const ventas = { id: 123 };
      jest.spyOn(ventasFormService, 'getVentas').mockReturnValue({ id: null });
      jest.spyOn(ventasService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ventas: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ventas }));
      saveSubject.complete();

      // THEN
      expect(ventasFormService.getVentas).toHaveBeenCalled();
      expect(ventasService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IVentas>>();
      const ventas = { id: 123 };
      jest.spyOn(ventasService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ventas });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ventasService.update).toHaveBeenCalled();
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
