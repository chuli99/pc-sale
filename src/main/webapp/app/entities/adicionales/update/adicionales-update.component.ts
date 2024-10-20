import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AdicionalesFormService, AdicionalesFormGroup } from './adicionales-form.service';
import { IAdicionales } from '../adicionales.model';
import { AdicionalesService } from '../service/adicionales.service';
import { IDispositivos } from 'app/entities/dispositivos/dispositivos.model';
import { DispositivosService } from 'app/entities/dispositivos/service/dispositivos.service';

@Component({
  selector: 'jhi-adicionales-update',
  templateUrl: './adicionales-update.component.html',
})
export class AdicionalesUpdateComponent implements OnInit {
  isSaving = false;
  adicionales: IAdicionales | null = null;

  dispositivosSharedCollection: IDispositivos[] = [];

  editForm: AdicionalesFormGroup = this.adicionalesFormService.createAdicionalesFormGroup();

  constructor(
    protected adicionalesService: AdicionalesService,
    protected adicionalesFormService: AdicionalesFormService,
    protected dispositivosService: DispositivosService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDispositivos = (o1: IDispositivos | null, o2: IDispositivos | null): boolean =>
    this.dispositivosService.compareDispositivos(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adicionales }) => {
      this.adicionales = adicionales;
      if (adicionales) {
        this.updateForm(adicionales);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const adicionales = this.adicionalesFormService.getAdicionales(this.editForm);
    if (adicionales.id !== null) {
      this.subscribeToSaveResponse(this.adicionalesService.update(adicionales));
    } else {
      this.subscribeToSaveResponse(this.adicionalesService.create(adicionales));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdicionales>>): void {
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

  protected updateForm(adicionales: IAdicionales): void {
    this.adicionales = adicionales;
    this.adicionalesFormService.resetForm(this.editForm, adicionales);

    this.dispositivosSharedCollection = this.dispositivosService.addDispositivosToCollectionIfMissing<IDispositivos>(
      this.dispositivosSharedCollection,
      adicionales.dispositivos
    );
  }

  protected loadRelationshipsOptions(): void {
    this.dispositivosService
      .query()
      .pipe(map((res: HttpResponse<IDispositivos[]>) => res.body ?? []))
      .pipe(
        map((dispositivos: IDispositivos[]) =>
          this.dispositivosService.addDispositivosToCollectionIfMissing<IDispositivos>(dispositivos, this.adicionales?.dispositivos)
        )
      )
      .subscribe((dispositivos: IDispositivos[]) => (this.dispositivosSharedCollection = dispositivos));
  }
}
