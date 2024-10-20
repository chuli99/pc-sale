import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { DispositivosFormService, DispositivosFormGroup } from './dispositivos-form.service';
import { IDispositivos } from '../dispositivos.model';
import { DispositivosService } from '../service/dispositivos.service';

@Component({
  selector: 'jhi-dispositivos-update',
  templateUrl: './dispositivos-update.component.html',
})
export class DispositivosUpdateComponent implements OnInit {
  isSaving = false;
  dispositivos: IDispositivos | null = null;

  editForm: DispositivosFormGroup = this.dispositivosFormService.createDispositivosFormGroup();

  constructor(
    protected dispositivosService: DispositivosService,
    protected dispositivosFormService: DispositivosFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dispositivos }) => {
      this.dispositivos = dispositivos;
      if (dispositivos) {
        this.updateForm(dispositivos);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dispositivos = this.dispositivosFormService.getDispositivos(this.editForm);
    if (dispositivos.id !== null) {
      this.subscribeToSaveResponse(this.dispositivosService.update(dispositivos));
    } else {
      this.subscribeToSaveResponse(this.dispositivosService.create(dispositivos));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDispositivos>>): void {
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

  protected updateForm(dispositivos: IDispositivos): void {
    this.dispositivos = dispositivos;
    this.dispositivosFormService.resetForm(this.editForm, dispositivos);
  }
}
