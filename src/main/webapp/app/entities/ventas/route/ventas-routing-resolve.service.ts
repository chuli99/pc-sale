import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVentas } from '../ventas.model';
import { VentasService } from '../service/ventas.service';

@Injectable({ providedIn: 'root' })
export class VentasRoutingResolveService implements Resolve<IVentas | null> {
  constructor(protected service: VentasService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVentas | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ventas: HttpResponse<IVentas>) => {
          if (ventas.body) {
            return of(ventas.body);
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
