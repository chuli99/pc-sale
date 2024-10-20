import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOpciones } from '../opciones.model';

@Component({
  selector: 'jhi-opciones-detail',
  templateUrl: './opciones-detail.component.html',
})
export class OpcionesDetailComponent implements OnInit {
  opciones: IOpciones | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ opciones }) => {
      this.opciones = opciones;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
