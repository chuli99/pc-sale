import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAdicionales } from '../adicionales.model';

@Component({
  selector: 'jhi-adicionales-detail',
  templateUrl: './adicionales-detail.component.html',
})
export class AdicionalesDetailComponent implements OnInit {
  adicionales: IAdicionales | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ adicionales }) => {
      this.adicionales = adicionales;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
