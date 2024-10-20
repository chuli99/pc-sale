import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PersonalizacionesFormService, PersonalizacionesFormGroup } from './personalizaciones-form.service';
import { IPersonalizaciones } from '../personalizaciones.model';
import { PersonalizacionesService } from '../service/personalizaciones.service';
import { IDispositivos } from 'app/entities/dispositivos/dispositivos.model';
import { DispositivosService } from 'app/entities/dispositivos/service/dispositivos.service';

@Component({
  selector: 'jhi-personalizaciones-update',
  templateUrl: './personalizaciones-update.component.html',
})
export class PersonalizacionesUpdateComponent implements OnInit {
  isSaving = false;
  personalizaciones: IPersonalizaciones | null = null;

  dispositivosSharedCollection: IDispositivos[] = [];

  editForm: PersonalizacionesFormGroup = this.personalizacionesFormService.createPersonalizacionesFormGroup();

  constructor(
    protected personalizacionesService: PersonalizacionesService,
    protected personalizacionesFormService: PersonalizacionesFormService,
    protected dispositivosService: DispositivosService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDispositivos = (o1: IDispositivos | null, o2: IDispositivos | null): boolean =>
    this.dispositivosService.compareDispositivos(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personalizaciones }) => {
      this.personalizaciones = personalizaciones;
      if (personalizaciones) {
        this.updateForm(personalizaciones);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personalizaciones = this.personalizacionesFormService.getPersonalizaciones(this.editForm);
    if (personalizaciones.id !== null) {
      this.subscribeToSaveResponse(this.personalizacionesService.update(personalizaciones));
    } else {
      this.subscribeToSaveResponse(this.personalizacionesService.create(personalizaciones));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonalizaciones>>): void {
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

  protected updateForm(personalizaciones: IPersonalizaciones): void {
    this.personalizaciones = personalizaciones;
    this.personalizacionesFormService.resetForm(this.editForm, personalizaciones);

    this.dispositivosSharedCollection = this.dispositivosService.addDispositivosToCollectionIfMissing<IDispositivos>(
      this.dispositivosSharedCollection,
      personalizaciones.dispositivos
    );
  }

  protected loadRelationshipsOptions(): void {
    this.dispositivosService
      .query()
      .pipe(map((res: HttpResponse<IDispositivos[]>) => res.body ?? []))
      .pipe(
        map((dispositivos: IDispositivos[]) =>
          this.dispositivosService.addDispositivosToCollectionIfMissing<IDispositivos>(dispositivos, this.personalizaciones?.dispositivos)
        )
      )
      .subscribe((dispositivos: IDispositivos[]) => (this.dispositivosSharedCollection = dispositivos));
  }
}
