import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDispositivos } from '../dispositivos.model';

@Component({
  selector: 'jhi-dispositivos-detail',
  templateUrl: './dispositivos-detail.component.html',
})
export class DispositivosDetailComponent implements OnInit {
  dispositivos: IDispositivos | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dispositivos }) => {
      this.dispositivos = dispositivos;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
