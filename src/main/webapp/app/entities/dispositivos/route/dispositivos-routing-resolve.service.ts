import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDispositivos } from '../dispositivos.model';
import { DispositivosService } from '../service/dispositivos.service';

@Injectable({ providedIn: 'root' })
export class DispositivosRoutingResolveService implements Resolve<IDispositivos | null> {
  constructor(protected service: DispositivosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDispositivos | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dispositivos: HttpResponse<IDispositivos>) => {
          if (dispositivos.body) {
            return of(dispositivos.body);
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
