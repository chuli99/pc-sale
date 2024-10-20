import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDispositivos, NewDispositivos } from '../dispositivos.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDispositivos for edit and NewDispositivosFormGroupInput for create.
 */
type DispositivosFormGroupInput = IDispositivos | PartialWithRequiredKeyOf<NewDispositivos>;

type DispositivosFormDefaults = Pick<NewDispositivos, 'id'>;

type DispositivosFormGroupContent = {
  id: FormControl<IDispositivos['id'] | NewDispositivos['id']>;
  codigo: FormControl<IDispositivos['codigo']>;
  nombre: FormControl<IDispositivos['nombre']>;
  descripcion: FormControl<IDispositivos['descripcion']>;
  precioBase: FormControl<IDispositivos['precioBase']>;
  moneda: FormControl<IDispositivos['moneda']>;
};

export type DispositivosFormGroup = FormGroup<DispositivosFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DispositivosFormService {
  createDispositivosFormGroup(dispositivos: DispositivosFormGroupInput = { id: null }): DispositivosFormGroup {
    const dispositivosRawValue = {
      ...this.getFormDefaults(),
      ...dispositivos,
    };
    return new FormGroup<DispositivosFormGroupContent>({
      id: new FormControl(
        { value: dispositivosRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      codigo: new FormControl(dispositivosRawValue.codigo, {
        validators: [Validators.required],
      }),
      nombre: new FormControl(dispositivosRawValue.nombre, {
        validators: [Validators.required],
      }),
      descripcion: new FormControl(dispositivosRawValue.descripcion, {
        validators: [Validators.required],
      }),
      precioBase: new FormControl(dispositivosRawValue.precioBase, {
        validators: [Validators.required, Validators.min(0)],
      }),
      moneda: new FormControl(dispositivosRawValue.moneda, {
        validators: [Validators.required],
      }),
    });
  }

  getDispositivos(form: DispositivosFormGroup): IDispositivos | NewDispositivos {
    return form.getRawValue() as IDispositivos | NewDispositivos;
  }

  resetForm(form: DispositivosFormGroup, dispositivos: DispositivosFormGroupInput): void {
    const dispositivosRawValue = { ...this.getFormDefaults(), ...dispositivos };
    form.reset(
      {
        ...dispositivosRawValue,
        id: { value: dispositivosRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): DispositivosFormDefaults {
    return {
      id: null,
    };
  }
}
