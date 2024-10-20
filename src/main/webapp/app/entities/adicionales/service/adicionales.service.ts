import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAdicionales, NewAdicionales } from '../adicionales.model';

export type PartialUpdateAdicionales = Partial<IAdicionales> & Pick<IAdicionales, 'id'>;

export type EntityResponseType = HttpResponse<IAdicionales>;
export type EntityArrayResponseType = HttpResponse<IAdicionales[]>;

@Injectable({ providedIn: 'root' })
export class AdicionalesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/adicionales');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(adicionales: NewAdicionales): Observable<EntityResponseType> {
    return this.http.post<IAdicionales>(this.resourceUrl, adicionales, { observe: 'response' });
  }

  update(adicionales: IAdicionales): Observable<EntityResponseType> {
    return this.http.put<IAdicionales>(`${this.resourceUrl}/${this.getAdicionalesIdentifier(adicionales)}`, adicionales, {
      observe: 'response',
    });
  }

  partialUpdate(adicionales: PartialUpdateAdicionales): Observable<EntityResponseType> {
    return this.http.patch<IAdicionales>(`${this.resourceUrl}/${this.getAdicionalesIdentifier(adicionales)}`, adicionales, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAdicionales>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAdicionales[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAdicionalesIdentifier(adicionales: Pick<IAdicionales, 'id'>): number {
    return adicionales.id;
  }

  compareAdicionales(o1: Pick<IAdicionales, 'id'> | null, o2: Pick<IAdicionales, 'id'> | null): boolean {
    return o1 && o2 ? this.getAdicionalesIdentifier(o1) === this.getAdicionalesIdentifier(o2) : o1 === o2;
  }

  addAdicionalesToCollectionIfMissing<Type extends Pick<IAdicionales, 'id'>>(
    adicionalesCollection: Type[],
    ...adicionalesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const adicionales: Type[] = adicionalesToCheck.filter(isPresent);
    if (adicionales.length > 0) {
      const adicionalesCollectionIdentifiers = adicionalesCollection.map(
        adicionalesItem => this.getAdicionalesIdentifier(adicionalesItem)!
      );
      const adicionalesToAdd = adicionales.filter(adicionalesItem => {
        const adicionalesIdentifier = this.getAdicionalesIdentifier(adicionalesItem);
        if (adicionalesCollectionIdentifiers.includes(adicionalesIdentifier)) {
          return false;
        }
        adicionalesCollectionIdentifiers.push(adicionalesIdentifier);
        return true;
      });
      return [...adicionalesToAdd, ...adicionalesCollection];
    }
    return adicionalesCollection;
  }
}
