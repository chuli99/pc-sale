import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { VentasFormService, VentasFormGroup } from './ventas-form.service';
import { IVentas } from '../ventas.model';
import { VentasService } from '../service/ventas.service';
import { IDispositivos } from 'app/entities/dispositivos/dispositivos.model';
import { DispositivosService } from 'app/entities/dispositivos/service/dispositivos.service';

@Component({
  selector: 'jhi-ventas-update',
  templateUrl: './ventas-update.component.html',
})
export class VentasUpdateComponent implements OnInit {
  isSaving = false;
  ventas: IVentas | null = null;

  dispositivosSharedCollection: IDispositivos[] = [];

  editForm: VentasFormGroup = this.ventasFormService.createVentasFormGroup();

  constructor(
    protected ventasService: VentasService,
    protected ventasFormService: VentasFormService,
    protected dispositivosService: DispositivosService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDispositivos = (o1: IDispositivos | null, o2: IDispositivos | null): boolean =>
    this.dispositivosService.compareDispositivos(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ventas }) => {
      this.ventas = ventas;
      if (ventas) {
        this.updateForm(ventas);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ventas = this.ventasFormService.getVentas(this.editForm);
    if (ventas.id !== null) {
      this.subscribeToSaveResponse(this.ventasService.update(ventas));
    } else {
      this.subscribeToSaveResponse(this.ventasService.create(ventas));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVentas>>): void {
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

  protected updateForm(ventas: IVentas): void {
    this.ventas = ventas;
    this.ventasFormService.resetForm(this.editForm, ventas);

    this.dispositivosSharedCollection = this.dispositivosService.addDispositivosToCollectionIfMissing<IDispositivos>(
      this.dispositivosSharedCollection,
      ventas.idDispositivo
    );
  }

  protected loadRelationshipsOptions(): void {
    this.dispositivosService
      .query()
      .pipe(map((res: HttpResponse<IDispositivos[]>) => res.body ?? []))
      .pipe(
        map((dispositivos: IDispositivos[]) =>
          this.dispositivosService.addDispositivosToCollectionIfMissing<IDispositivos>(dispositivos, this.ventas?.idDispositivo)
        )
      )
      .subscribe((dispositivos: IDispositivos[]) => (this.dispositivosSharedCollection = dispositivos));
  }
}
