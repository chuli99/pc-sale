import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../caracteristicas.test-samples';

import { CaracteristicasFormService } from './caracteristicas-form.service';

describe('Caracteristicas Form Service', () => {
  let service: CaracteristicasFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CaracteristicasFormService);
  });

  describe('Service methods', () => {
    describe('createCaracteristicasFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCaracteristicasFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            dispositivos: expect.any(Object),
          })
        );
      });

      it('passing ICaracteristicas should create a new form with FormGroup', () => {
        const formGroup = service.createCaracteristicasFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            dispositivos: expect.any(Object),
          })
        );
      });
    });

    describe('getCaracteristicas', () => {
      it('should return NewCaracteristicas for default Caracteristicas initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createCaracteristicasFormGroup(sampleWithNewData);

        const caracteristicas = service.getCaracteristicas(formGroup) as any;

        expect(caracteristicas).toMatchObject(sampleWithNewData);
      });

      it('should return NewCaracteristicas for empty Caracteristicas initial value', () => {
        const formGroup = service.createCaracteristicasFormGroup();

        const caracteristicas = service.getCaracteristicas(formGroup) as any;

        expect(caracteristicas).toMatchObject({});
      });

      it('should return ICaracteristicas', () => {
        const formGroup = service.createCaracteristicasFormGroup(sampleWithRequiredData);

        const caracteristicas = service.getCaracteristicas(formGroup) as any;

        expect(caracteristicas).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICaracteristicas should not enable id FormControl', () => {
        const formGroup = service.createCaracteristicasFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCaracteristicas should disable id FormControl', () => {
        const formGroup = service.createCaracteristicasFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
