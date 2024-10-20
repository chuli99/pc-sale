import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../personalizaciones.test-samples';

import { PersonalizacionesFormService } from './personalizaciones-form.service';

describe('Personalizaciones Form Service', () => {
  let service: PersonalizacionesFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PersonalizacionesFormService);
  });

  describe('Service methods', () => {
    describe('createPersonalizacionesFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPersonalizacionesFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nombre: expect.any(Object),
            descripcion: expect.any(Object),
            dispositivos: expect.any(Object),
          })
        );
      });

      it('passing IPersonalizaciones should create a new form with FormGroup', () => {
        const formGroup = service.createPersonalizacionesFormGroup(sampleWithRequiredData);

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

    describe('getPersonalizaciones', () => {
      it('should return NewPersonalizaciones for default Personalizaciones initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPersonalizacionesFormGroup(sampleWithNewData);

        const personalizaciones = service.getPersonalizaciones(formGroup) as any;

        expect(personalizaciones).toMatchObject(sampleWithNewData);
      });

      it('should return NewPersonalizaciones for empty Personalizaciones initial value', () => {
        const formGroup = service.createPersonalizacionesFormGroup();

        const personalizaciones = service.getPersonalizaciones(formGroup) as any;

        expect(personalizaciones).toMatchObject({});
      });

      it('should return IPersonalizaciones', () => {
        const formGroup = service.createPersonalizacionesFormGroup(sampleWithRequiredData);

        const personalizaciones = service.getPersonalizaciones(formGroup) as any;

        expect(personalizaciones).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPersonalizaciones should not enable id FormControl', () => {
        const formGroup = service.createPersonalizacionesFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPersonalizaciones should disable id FormControl', () => {
        const formGroup = service.createPersonalizacionesFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
