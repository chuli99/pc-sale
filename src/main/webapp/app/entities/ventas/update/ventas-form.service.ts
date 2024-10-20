import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVentas, NewVentas } from '../ventas.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IVentas for edit and NewVentasFormGroupInput for create.
 */
type VentasFormGroupInput = IVentas | PartialWithRequiredKeyOf<NewVentas>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IVentas | NewVentas> = Omit<T, 'fechaVenta'> & {
  fechaVenta?: string | null;
};

type VentasFormRawValue = FormValueOf<IVentas>;

type NewVentasFormRawValue = FormValueOf<NewVentas>;

type VentasFormDefaults = Pick<NewVentas, 'id' | 'fechaVenta'>;

type VentasFormGroupContent = {
  id: FormControl<VentasFormRawValue['id'] | NewVentas['id']>;
  precioFinal: FormControl<VentasFormRawValue['precioFinal']>;
  fechaVenta: FormControl<VentasFormRawValue['fechaVenta']>;
  idDispositivo: FormControl<VentasFormRawValue['idDispositivo']>;
};

export type VentasFormGroup = FormGroup<VentasFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class VentasFormService {
  createVentasFormGroup(ventas: VentasFormGroupInput = { id: null }): VentasFormGroup {
    const ventasRawValue = this.convertVentasToVentasRawValue({
      ...this.getFormDefaults(),
      ...ventas,
    });
    return new FormGroup<VentasFormGroupContent>({
      id: new FormControl(
        { value: ventasRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      precioFinal: new FormControl(ventasRawValue.precioFinal, {
        validators: [Validators.required, Validators.min(0)],
      }),
      fechaVenta: new FormControl(ventasRawValue.fechaVenta, {
        validators: [Validators.required],
      }),
      idDispositivo: new FormControl(ventasRawValue.idDispositivo),
    });
  }

  getVentas(form: VentasFormGroup): IVentas | NewVentas {
    return this.convertVentasRawValueToVentas(form.getRawValue() as VentasFormRawValue | NewVentasFormRawValue);
  }

  resetForm(form: VentasFormGroup, ventas: VentasFormGroupInput): void {
    const ventasRawValue = this.convertVentasToVentasRawValue({ ...this.getFormDefaults(), ...ventas });
    form.reset(
      {
        ...ventasRawValue,
        id: { value: ventasRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): VentasFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      fechaVenta: currentTime,
    };
  }

  private convertVentasRawValueToVentas(rawVentas: VentasFormRawValue | NewVentasFormRawValue): IVentas | NewVentas {
    return {
      ...rawVentas,
      fechaVenta: dayjs(rawVentas.fechaVenta, DATE_TIME_FORMAT),
    };
  }

  private convertVentasToVentasRawValue(
    ventas: IVentas | (Partial<NewVentas> & VentasFormDefaults)
  ): VentasFormRawValue | PartialWithRequiredKeyOf<NewVentasFormRawValue> {
    return {
      ...ventas,
      fechaVenta: ventas.fechaVenta ? ventas.fechaVenta.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
