import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { CaracteristicasFormService, CaracteristicasFormGroup } from './caracteristicas-form.service';
import { ICaracteristicas } from '../caracteristicas.model';
import { CaracteristicasService } from '../service/caracteristicas.service';
import { IDispositivos } from 'app/entities/dispositivos/dispositivos.model';
import { DispositivosService } from 'app/entities/dispositivos/service/dispositivos.service';

@Component({
  selector: 'jhi-caracteristicas-update',
  templateUrl: './caracteristicas-update.component.html',
})
export class CaracteristicasUpdateComponent implements OnInit {
  isSaving = false;
  caracteristicas: ICaracteristicas | null = null;

  dispositivosSharedCollection: IDispositivos[] = [];

  editForm: CaracteristicasFormGroup = this.caracteristicasFormService.createCaracteristicasFormGroup();

  constructor(
    protected caracteristicasService: CaracteristicasService,
    protected caracteristicasFormService: CaracteristicasFormService,
    protected dispositivosService: DispositivosService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDispositivos = (o1: IDispositivos | null, o2: IDispositivos | null): boolean =>
    this.dispositivosService.compareDispositivos(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ caracteristicas }) => {
      this.caracteristicas = caracteristicas;
      if (caracteristicas) {
        this.updateForm(caracteristicas);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const caracteristicas = this.caracteristicasFormService.getCaracteristicas(this.editForm);
    if (caracteristicas.id !== null) {
      this.subscribeToSaveResponse(this.caracteristicasService.update(caracteristicas));
    } else {
      this.subscribeToSaveResponse(this.caracteristicasService.create(caracteristicas));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICaracteristicas>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(caracteristicas: ICaracteristicas): void {
    this.caracteristicas = caracteristicas;
    this.caracteristicasFormService.resetForm(this.editForm, caracteristicas);

    this.dispositivosSharedCollection = this.dispositivosService.addDispositivosToCollectionIfMissing<IDispositivos>(
      this.dispositivosSharedCollection,
      caracteristicas.dispositivos
    );
  }

  protected loadRelationshipsOptions(): void {
    this.dispositivosService
      .query()
      .pipe(map((res: HttpResponse<IDispositivos[]>) => res.body ?? []))
      .pipe(
        map((dispositivos: IDispositivos[]) =>
          this.dispositivosService.addDispositivosToCollectionIfMissing<IDispositivos>(dispositivos, this.caracteristicas?.dispositivos)
        )
      )
      .subscribe((dispositivos: IDispositivos[]) => (this.dispositivosSharedCollection = dispositivos));
  }
}
