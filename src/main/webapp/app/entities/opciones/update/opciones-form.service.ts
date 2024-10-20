import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOpciones, NewOpciones } from '../opciones.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOpciones for edit and NewOpcionesFormGroupInput for create.
 */
type OpcionesFormGroupInput = IOpciones | PartialWithRequiredKeyOf<NewOpciones>;

type OpcionesFormDefaults = Pick<NewOpciones, 'id'>;

type OpcionesFormGroupContent = {
  id: FormControl<IOpciones['id'] | NewOpciones['id']>;
  codigo: FormControl<IOpciones['codigo']>;
  nombre: FormControl<IOpciones['nombre']>;
  descripcion: FormControl<IOpciones['descripcion']>;
  precioAdicional: FormControl<IOpciones['precioAdicional']>;
  personalizaciones: FormControl<IOpciones['personalizaciones']>;
};

export type OpcionesFormGroup = FormGroup<OpcionesFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OpcionesFormService {
  createOpcionesFormGroup(opciones: OpcionesFormGroupInput = { id: null }): OpcionesFormGroup {
    const opcionesRawValue = {
      ...this.getFormDefaults(),
      ...opciones,
    };
    return new FormGroup<OpcionesFormGroupContent>({
      id: new FormControl(
        { value: opcionesRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      codigo: new FormControl(opcionesRawValue.codigo, {
        validators: [Validators.required],
      }),
      nombre: new FormControl(opcionesRawValue.nombre, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(opcionesRawValue.descripcion, {
        validators: [Validators.required],
      }),
      precioAdicional: new FormControl(opcionesRawValue.precioAdicional, {
        validators: [Validators.required, Validators.min(0)],
      }),
      personalizaciones: new FormControl(opcionesRawValue.personalizaciones),
    });
  }

  getOpciones(form: OpcionesFormGroup): IOpciones | NewOpciones {
    return form.getRawValue() as IOpciones | NewOpciones;
  }

  resetForm(form: OpcionesFormGroup, opciones: OpcionesFormGroupInput): void {
    const opcionesRawValue = { ...this.getFormDefaults(), ...opciones };
    form.reset(
      {
        ...opcionesRawValue,
        id: { value: opcionesRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OpcionesFormDefaults {
    return {
      id: null,
    };
  }
}
