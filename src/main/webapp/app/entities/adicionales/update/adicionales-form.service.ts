import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAdicionales, NewAdicionales } from '../adicionales.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAdicionales for edit and NewAdicionalesFormGroupInput for create.
 */
type AdicionalesFormGroupInput = IAdicionales | PartialWithRequiredKeyOf<NewAdicionales>;

type AdicionalesFormDefaults = Pick<NewAdicionales, 'id'>;

type AdicionalesFormGroupContent = {
  id: FormControl<IAdicionales['id'] | NewAdicionales['id']>;
  nombre: FormControl<IAdicionales['nombre']>;
  descripcion: FormControl<IAdicionales['descripcion']>;
  precio: FormControl<IAdicionales['precio']>;
  precioGratis: FormControl<IAdicionales['precioGratis']>;
  dispositivos: FormControl<IAdicionales['dispositivos']>;
};

export type AdicionalesFormGroup = FormGroup<AdicionalesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AdicionalesFormService {
  createAdicionalesFormGroup(adicionales: AdicionalesFormGroupInput = { id: null }): AdicionalesFormGroup {
    const adicionalesRawValue = {
      ...this.getFormDefaults(),
      ...adicionales,
    };
    return new FormGroup<AdicionalesFormGroupContent>({
      id: new FormControl(
        { value: adicionalesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(adicionalesRawValue.nombre, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(adicionalesRawValue.descripcion, {
        validators: [Validators.required],
      }),
      precio: new FormControl(adicionalesRawValue.precio, {
        validators: [Validators.required, Validators.min(0)],
      }),
      precioGratis: new FormControl(adicionalesRawValue.precioGratis, {
        validators: [Validators.required],
      }),
      dispositivos: new FormControl(adicionalesRawValue.dispositivos),
    });
  }

  getAdicionales(form: AdicionalesFormGroup): IAdicionales | NewAdicionales {
    return form.getRawValue() as IAdicionales | NewAdicionales;
  }

  resetForm(form: AdicionalesFormGroup, adicionales: AdicionalesFormGroupInput): void {
    const adicionalesRawValue = { ...this.getFormDefaults(), ...adicionales };
    form.reset(
      {
        ...adicionalesRawValue,
        id: { value: adicionalesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AdicionalesFormDefaults {
    return {
      id: null,
    };
  }
}
