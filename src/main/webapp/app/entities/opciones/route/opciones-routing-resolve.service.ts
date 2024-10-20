import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOpciones } from '../opciones.model';
import { OpcionesService } from '../service/opciones.service';

@Injectable({ providedIn: 'root' })
export class OpcionesRoutingResolveService implements Resolve<IOpciones | null> {
  constructor(protected service: OpcionesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOpciones | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((opciones: HttpResponse<IOpciones>) => {
          if (opciones.body) {
            return of(opciones.body);
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
