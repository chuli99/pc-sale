import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICaracteristicas } from '../caracteristicas.model';

@Component({
  selector: 'jhi-caracteristicas-detail',
  templateUrl: './caracteristicas-detail.component.html',
})
export class CaracteristicasDetailComponent implements OnInit {
  caracteristicas: ICaracteristicas | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ caracteristicas }) => {
      this.caracteristicas = caracteristicas;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
