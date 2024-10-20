import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DispositivosFormService } from './dispositivos-form.service';
import { DispositivosService } from '../service/dispositivos.service';
import { IDispositivos } from '../dispositivos.model';

import { DispositivosUpdateComponent } from './dispositivos-update.component';

describe('Dispositivos Management Update Component', () => {
  let comp: DispositivosUpdateComponent;
  let fixture: ComponentFixture<DispositivosUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dispositivosFormService: DispositivosFormService;
  let dispositivosService: DispositivosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DispositivosUpdateComponent],
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
      .overrideTemplate(DispositivosUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DispositivosUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dispositivosFormService = TestBed.inject(DispositivosFormService);
    dispositivosService = TestBed.inject(DispositivosService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const dispositivos: IDispositivos = { id: 456 };

      activatedRoute.data = of({ dispositivos });
      comp.ngOnInit();

      expect(comp.dispositivos).toEqual(dispositivos);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDispositivos>>();
      const dispositivos = { id: 123 };
      jest.spyOn(dispositivosFormService, 'getDispositivos').mockReturnValue(dispositivos);
      jest.spyOn(dispositivosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dispositivos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dispositivos }));
      saveSubject.complete();

      // THEN
      expect(dispositivosFormService.getDispositivos).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dispositivosService.update).toHaveBeenCalledWith(expect.objectContaining(dispositivos));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDispositivos>>();
      const dispositivos = { id: 123 };
      jest.spyOn(dispositivosFormService, 'getDispositivos').mockReturnValue({ id: null });
      jest.spyOn(dispositivosService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dispositivos: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dispositivos }));
      saveSubject.complete();

      // THEN
      expect(dispositivosFormService.getDispositivos).toHaveBeenCalled();
      expect(dispositivosService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDispositivos>>();
      const dispositivos = { id: 123 };
      jest.spyOn(dispositivosService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dispositivos });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dispositivosService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
