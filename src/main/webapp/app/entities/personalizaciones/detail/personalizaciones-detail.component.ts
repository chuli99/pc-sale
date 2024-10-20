import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonalizaciones } from '../personalizaciones.model';

@Component({
  selector: 'jhi-personalizaciones-detail',
  templateUrl: './personalizaciones-detail.component.html',
})
export class PersonalizacionesDetailComponent implements OnInit {
  personalizaciones: IPersonalizaciones | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personalizaciones }) => {
      this.personalizaciones = personalizaciones;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
