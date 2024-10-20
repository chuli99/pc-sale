import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICaracteristicas, NewCaracteristicas } from '../caracteristicas.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICaracteristicas for edit and NewCaracteristicasFormGroupInput for create.
 */
type CaracteristicasFormGroupInput = ICaracteristicas | PartialWithRequiredKeyOf<NewCaracteristicas>;

type CaracteristicasFormDefaults = Pick<NewCaracteristicas, 'id'>;

type CaracteristicasFormGroupContent = {
  id: FormControl<ICaracteristicas['id'] | NewCaracteristicas['id']>;
  nombre: FormControl<ICaracteristicas['nombre']>;
  descripcion: FormControl<ICaracteristicas['descripcion']>;
  dispositivos: FormControl<ICaracteristicas['dispositivos']>;
};

export type CaracteristicasFormGroup = FormGroup<CaracteristicasFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CaracteristicasFormService {
  createCaracteristicasFormGroup(caracteristicas: CaracteristicasFormGroupInput = { id: null }): CaracteristicasFormGroup {
    const caracteristicasRawValue = {
      ...this.getFormDefaults(),
      ...caracteristicas,
    };
    return new FormGroup<CaracteristicasFormGroupContent>({
      id: new FormControl(
        { value: caracteristicasRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(caracteristicasRawValue.nombre, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(caracteristicasRawValue.descripcion, {
        validators: [Validators.required],
      }),
      dispositivos: new FormControl(caracteristicasRawValue.dispositivos),
    });
  }

  getCaracteristicas(form: CaracteristicasFormGroup): ICaracteristicas | NewCaracteristicas {
    return form.getRawValue() as ICaracteristicas | NewCaracteristicas;
  }

  resetForm(form: CaracteristicasFormGroup, caracteristicas: CaracteristicasFormGroupInput): void {
    const caracteristicasRawValue = { ...this.getFormDefaults(), ...caracteristicas };
    form.reset(
      {
        ...caracteristicasRawValue,
        id: { value: caracteristicasRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CaracteristicasFormDefaults {
    return {
      id: null,
    };
  }
}
