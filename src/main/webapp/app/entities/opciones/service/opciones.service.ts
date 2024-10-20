import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOpciones, NewOpciones } from '../opciones.model';

export type PartialUpdateOpciones = Partial<IOpciones> & Pick<IOpciones, 'id'>;

export type EntityResponseType = HttpResponse<IOpciones>;
export type EntityArrayResponseType = HttpResponse<IOpciones[]>;

@Injectable({ providedIn: 'root' })
export class OpcionesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/opciones');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(opciones: NewOpciones): Observable<EntityResponseType> {
    return this.http.post<IOpciones>(this.resourceUrl, opciones, { observe: 'response' });
  }

  update(opciones: IOpciones): Observable<EntityResponseType> {
    return this.http.put<IOpciones>(`${this.resourceUrl}/${this.getOpcionesIdentifier(opciones)}`, opciones, { observe: 'response' });
  }

  partialUpdate(opciones: PartialUpdateOpciones): Observable<EntityResponseType> {
    return this.http.patch<IOpciones>(`${this.resourceUrl}/${this.getOpcionesIdentifier(opciones)}`, opciones, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOpciones>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOpciones[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOpcionesIdentifier(opciones: Pick<IOpciones, 'id'>): number {
    return opciones.id;
  }

  compareOpciones(o1: Pick<IOpciones, 'id'> | null, o2: Pick<IOpciones, 'id'> | null): boolean {
    return o1 && o2 ? this.getOpcionesIdentifier(o1) === this.getOpcionesIdentifier(o2) : o1 === o2;
  }

  addOpcionesToCollectionIfMissing<Type extends Pick<IOpciones, 'id'>>(
    opcionesCollection: Type[],
    ...opcionesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const opciones: Type[] = opcionesToCheck.filter(isPresent);
    if (opciones.length > 0) {
      const opcionesCollectionIdentifiers = opcionesCollection.map(opcionesItem => this.getOpcionesIdentifier(opcionesItem)!);
      const opcionesToAdd = opciones.filter(opcionesItem => {
        const opcionesIdentifier = this.getOpcionesIdentifier(opcionesItem);
        if (opcionesCollectionIdentifiers.includes(opcionesIdentifier)) {
          return false;
        }
        opcionesCollectionIdentifiers.push(opcionesIdentifier);
        return true;
      });
      return [...opcionesToAdd, ...opcionesCollection];
    }
    return opcionesCollection;
  }
}
