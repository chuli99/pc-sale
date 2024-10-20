import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../opciones.test-samples';

import { OpcionesFormService } from './opciones-form.service';

describe('Opciones Form Service', () => {
  let service: OpcionesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OpcionesFormService);
  });

  describe('Service methods', () => {
    describe('createOpcionesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOpcionesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            precioAdicional: expect.any(Object),
            personalizaciones: expect.any(Object),
          })
        );
      });

      it('passing IOpciones should create a new form with FormGroup', () => {
        const formGroup = service.createOpcionesFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codigo: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            precioAdicional: expect.any(Object),
            personalizaciones: expect.any(Object),
          })
        );
      });
    });

    describe('getOpciones', () => {
      it('should return NewOpciones for default Opciones initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOpcionesFormGroup(sampleWithNewData);

        const opciones = service.getOpciones(formGroup) as any;

        expect(opciones).toMatchObject(sampleWithNewData);
      });

      it('should return NewOpciones for empty Opciones initial value', () => {
        const formGroup = service.createOpcionesFormGroup();

        const opciones = service.getOpciones(formGroup) as any;

        expect(opciones).toMatchObject({});
      });

      it('should return IOpciones', () => {
        const formGroup = service.createOpcionesFormGroup(sampleWithRequiredData);

        const opciones = service.getOpciones(formGroup) as any;

        expect(opciones).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOpciones should not enable id FormControl', () => {
        const formGroup = service.createOpcionesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOpciones should disable id FormControl', () => {
        const formGroup = service.createOpcionesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
