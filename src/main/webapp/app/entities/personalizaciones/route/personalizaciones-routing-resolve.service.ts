import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPersonalizaciones } from '../personalizaciones.model';
import { PersonalizacionesService } from '../service/personalizaciones.service';

@Injectable({ providedIn: 'root' })
export class PersonalizacionesRoutingResolveService implements Resolve<IPersonalizaciones | null> {
  constructor(protected service: PersonalizacionesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPersonalizaciones | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((personalizaciones: HttpResponse<IPersonalizaciones>) => {
          if (personalizaciones.body) {
            return of(personalizaciones.body);
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
