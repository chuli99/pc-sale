import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../adicionales.test-samples';

import { AdicionalesFormService } from './adicionales-form.service';

describe('Adicionales Form Service', () => {
  let service: AdicionalesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdicionalesFormService);
  });

  describe('Service methods', () => {
    describe('createAdicionalesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAdicionalesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            precio: expect.any(Object),
            precioGratis: expect.any(Object),
            dispositivos: expect.any(Object),
          })
        );
      });

      it('passing IAdicionales should create a new form with FormGroup', () => {
        const formGroup = service.createAdicionalesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            precio: expect.any(Object),
            precioGratis: expect.any(Object),
            dispositivos: expect.any(Object),
          })
        );
      });
    });

    describe('getAdicionales', () => {
      it('should return NewAdicionales for default Adicionales initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAdicionalesFormGroup(sampleWithNewData);

        const adicionales = service.getAdicionales(formGroup) as any;

        expect(adicionales).toMatchObject(sampleWithNewData);
      });

      it('should return NewAdicionales for empty Adicionales initial value', () => {
        const formGroup = service.createAdicionalesFormGroup();

        const adicionales = service.getAdicionales(formGroup) as any;

        expect(adicionales).toMatchObject({});
      });

      it('should return IAdicionales', () => {
        const formGroup = service.createAdicionalesFormGroup(sampleWithRequiredData);

        const adicionales = service.getAdicionales(formGroup) as any;

        expect(adicionales).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAdicionales should not enable id FormControl', () => {
        const formGroup = service.createAdicionalesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAdicionales should disable id FormControl', () => {
        const formGroup = service.createAdicionalesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
