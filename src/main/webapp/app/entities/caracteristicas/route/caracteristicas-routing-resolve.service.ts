import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICaracteristicas } from '../caracteristicas.model';
import { CaracteristicasService } from '../service/caracteristicas.service';

@Injectable({ providedIn: 'root' })
export class CaracteristicasRoutingResolveService implements Resolve<ICaracteristicas | null> {
  constructor(protected service: CaracteristicasService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICaracteristicas | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((caracteristicas: HttpResponse<ICaracteristicas>) => {
          if (caracteristicas.body) {
            return of(caracteristicas.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
