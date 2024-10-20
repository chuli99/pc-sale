import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../dispositivos.test-samples';

import { DispositivosFormService } from './dispositivos-form.service';

describe('Dispositivos Form Service', () => {
  let service: DispositivosFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DispositivosFormService);
  });

  describe('Service methods', () => {
    describe('createDispositivosFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDispositivosFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            precioBase: expect.any(Object),
            moneda: expect.any(Object),
          })
        );
      });

      it('passing IDispositivos should create a new form with FormGroup', () => {
        const formGroup = service.createDispositivosFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            precioBase: expect.any(Object),
            moneda: expect.any(Object),
          })
        );
      });
    });

    describe('getDispositivos', () => {
      it('should return NewDispositivos for default Dispositivos initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createDispositivosFormGroup(sampleWithNewData);

        const dispositivos = service.getDispositivos(formGroup) as any;

        expect(dispositivos).toMatchObject(sampleWithNewData);
      });

      it('should return NewDispositivos for empty Dispositivos initial value', () => {
        const formGroup = service.createDispositivosFormGroup();

        const dispositivos = service.getDispositivos(formGroup) as any;

        expect(dispositivos).toMatchObject({});
      });

      it('should return IDispositivos', () => {
        const formGroup = service.createDispositivosFormGroup(sampleWithRequiredData);

        const dispositivos = service.getDispositivos(formGroup) as any;

        expect(dispositivos).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDispositivos should not enable id FormControl', () => {
        const formGroup = service.createDispositivosFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDispositivos should disable id FormControl', () => {
        const formGroup = service.createDispositivosFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
