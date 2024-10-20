import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAdicionales } from '../adicionales.model';
import { AdicionalesService } from '../service/adicionales.service';

@Injectable({ providedIn: 'root' })
export class AdicionalesRoutingResolveService implements Resolve<IAdicionales | null> {
  constructor(protected service: AdicionalesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAdicionales | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((adicionales: HttpResponse<IAdicionales>) => {
          if (adicionales.body) {
            return of(adicionales.body);
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
