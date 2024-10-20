import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { OpcionesFormService, OpcionesFormGroup } from './opciones-form.service';
import { IOpciones } from '../opciones.model';
import { OpcionesService } from '../service/opciones.service';
import { IPersonalizaciones } from 'app/entities/personalizaciones/personalizaciones.model';
import { PersonalizacionesService } from 'app/entities/personalizaciones/service/personalizaciones.service';

@Component({
  selector: 'jhi-opciones-update',
  templateUrl: './opciones-update.component.html',
})
export class OpcionesUpdateComponent implements OnInit {
  isSaving = false;
  opciones: IOpciones | null = null;

  personalizacionesSharedCollection: IPersonalizaciones[] = [];

  editForm: OpcionesFormGroup = this.opcionesFormService.createOpcionesFormGroup();

  constructor(
    protected opcionesService: OpcionesService,
    protected opcionesFormService: OpcionesFormService,
    protected personalizacionesService: PersonalizacionesService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePersonalizaciones = (o1: IPersonalizaciones | null, o2: IPersonalizaciones | null): boolean =>
    this.personalizacionesService.comparePersonalizaciones(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ opciones }) => {
      this.opciones = opciones;
      if (opciones) {
        this.updateForm(opciones);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const opciones = this.opcionesFormService.getOpciones(this.editForm);
    if (opciones.id !== null) {
      this.subscribeToSaveResponse(this.opcionesService.update(opciones));
    } else {
      this.subscribeToSaveResponse(this.opcionesService.create(opciones));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOpciones>>): void {
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

  protected updateForm(opciones: IOpciones): void {
    this.opciones = opciones;
    this.opcionesFormService.resetForm(this.editForm, opciones);

    this.personalizacionesSharedCollection = this.personalizacionesService.addPersonalizacionesToCollectionIfMissing<IPersonalizaciones>(
      this.personalizacionesSharedCollection,
      opciones.personalizaciones
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personalizacionesService
      .query()
      .pipe(map((res: HttpResponse<IPersonalizaciones[]>) => res.body ?? []))
      .pipe(
        map((personalizaciones: IPersonalizaciones[]) =>
          this.personalizacionesService.addPersonalizacionesToCollectionIfMissing<IPersonalizaciones>(
            personalizaciones,
            this.opciones?.personalizaciones
          )
        )
      )
      .subscribe((personalizaciones: IPersonalizaciones[]) => (this.personalizacionesSharedCollection = personalizaciones));
  }
}
