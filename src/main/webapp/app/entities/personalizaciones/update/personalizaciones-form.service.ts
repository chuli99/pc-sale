import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPersonalizaciones, NewPersonalizaciones } from '../personalizaciones.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPersonalizaciones for edit and NewPersonalizacionesFormGroupInput for create.
 */
type PersonalizacionesFormGroupInput = IPersonalizaciones | PartialWithRequiredKeyOf<NewPersonalizaciones>;

type PersonalizacionesFormDefaults = Pick<NewPersonalizaciones, 'id'>;

type PersonalizacionesFormGroupContent = {
  id: FormControl<IPersonalizaciones['id'] | NewPersonalizaciones['id']>;
  nombre: FormControl<IPersonalizaciones['nombre']>;
  descripcion: FormControl<IPersonalizaciones['descripcion']>;
  dispositivos: FormControl<IPersonalizaciones['dispositivos']>;
};

export type PersonalizacionesFormGroup = FormGroup<PersonalizacionesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PersonalizacionesFormService {
  createPersonalizacionesFormGroup(personalizaciones: PersonalizacionesFormGroupInput = { id: null }): PersonalizacionesFormGroup {
    const personalizacionesRawValue = {
      ...this.getFormDefaults(),
      ...personalizaciones,
    };
    return new FormGroup<PersonalizacionesFormGroupContent>({
      id: new FormControl(
        { value: personalizacionesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nombre: new FormControl(personalizacionesRawValue.nombre, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(personalizacionesRawValue.descripcion, {
        validators: [Validators.required],
      }),
      dispositivos: new FormControl(personalizacionesRawValue.dispositivos),
    });
  }

  getPersonalizaciones(form: PersonalizacionesFormGroup): IPersonalizaciones | NewPersonalizaciones {
    return form.getRawValue() as IPersonalizaciones | NewPersonalizaciones;
  }

  resetForm(form: PersonalizacionesFormGroup, personalizaciones: PersonalizacionesFormGroupInput): void {
    const personalizacionesRawValue = { ...this.getFormDefaults(), ...personalizaciones };
    form.reset(
      {
        ...personalizacionesRawValue,
        id: { value: personalizacionesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PersonalizacionesFormDefaults {
    return {
      id: null,
    };
  }
}
