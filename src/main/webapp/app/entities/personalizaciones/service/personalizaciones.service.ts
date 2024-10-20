import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPersonalizaciones, NewPersonalizaciones } from '../personalizaciones.model';

export type PartialUpdatePersonalizaciones = Partial<IPersonalizaciones> & Pick<IPersonalizaciones, 'id'>;

export type EntityResponseType = HttpResponse<IPersonalizaciones>;
export type EntityArrayResponseType = HttpResponse<IPersonalizaciones[]>;

@Injectable({ providedIn: 'root' })
export class PersonalizacionesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/personalizaciones');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(personalizaciones: NewPersonalizaciones): Observable<EntityResponseType> {
    return this.http.post<IPersonalizaciones>(this.resourceUrl, personalizaciones, { observe: 'response' });
  }

  update(personalizaciones: IPersonalizaciones): Observable<EntityResponseType> {
    return this.http.put<IPersonalizaciones>(
      `${this.resourceUrl}/${this.getPersonalizacionesIdentifier(personalizaciones)}`,
      personalizaciones,
      { observe: 'response' }
    );
  }

  partialUpdate(personalizaciones: PartialUpdatePersonalizaciones): Observable<EntityResponseType> {
    return this.http.patch<IPersonalizaciones>(
      `${this.resourceUrl}/${this.getPersonalizacionesIdentifier(personalizaciones)}`,
      personalizaciones,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPersonalizaciones>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPersonalizaciones[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPersonalizacionesIdentifier(personalizaciones: Pick<IPersonalizaciones, 'id'>): number {
    return personalizaciones.id;
  }

  comparePersonalizaciones(o1: Pick<IPersonalizaciones, 'id'> | null, o2: Pick<IPersonalizaciones, 'id'> | null): boolean {
    return o1 && o2 ? this.getPersonalizacionesIdentifier(o1) === this.getPersonalizacionesIdentifier(o2) : o1 === o2;
  }

  addPersonalizacionesToCollectionIfMissing<Type extends Pick<IPersonalizaciones, 'id'>>(
    personalizacionesCollection: Type[],
    ...personalizacionesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const personalizaciones: Type[] = personalizacionesToCheck.filter(isPresent);
    if (personalizaciones.length > 0) {
      const personalizacionesCollectionIdentifiers = personalizacionesCollection.map(
        personalizacionesItem => this.getPersonalizacionesIdentifier(personalizacionesItem)!
      );
      const personalizacionesToAdd = personalizaciones.filter(personalizacionesItem => {
        const personalizacionesIdentifier = this.getPersonalizacionesIdentifier(personalizacionesItem);
        if (personalizacionesCollectionIdentifiers.includes(personalizacionesIdentifier)) {
          return false;
        }
        personalizacionesCollectionIdentifiers.push(personalizacionesIdentifier);
        return true;
      });
      return [...personalizacionesToAdd, ...personalizacionesCollection];
    }
    return personalizacionesCollection;
  }
}
