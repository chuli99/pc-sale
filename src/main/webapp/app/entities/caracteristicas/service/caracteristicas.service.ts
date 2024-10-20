import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICaracteristicas, NewCaracteristicas } from '../caracteristicas.model';

export type PartialUpdateCaracteristicas = Partial<ICaracteristicas> & Pick<ICaracteristicas, 'id'>;

export type EntityResponseType = HttpResponse<ICaracteristicas>;
export type EntityArrayResponseType = HttpResponse<ICaracteristicas[]>;

@Injectable({ providedIn: 'root' })
export class CaracteristicasService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/caracteristicas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(caracteristicas: NewCaracteristicas): Observable<EntityResponseType> {
    return this.http.post<ICaracteristicas>(this.resourceUrl, caracteristicas, { observe: 'response' });
  }

  update(caracteristicas: ICaracteristicas): Observable<EntityResponseType> {
    return this.http.put<ICaracteristicas>(`${this.resourceUrl}/${this.getCaracteristicasIdentifier(caracteristicas)}`, caracteristicas, {
      observe: 'response',
    });
  }

  partialUpdate(caracteristicas: PartialUpdateCaracteristicas): Observable<EntityResponseType> {
    return this.http.patch<ICaracteristicas>(`${this.resourceUrl}/${this.getCaracteristicasIdentifier(caracteristicas)}`, caracteristicas, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICaracteristicas>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICaracteristicas[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCaracteristicasIdentifier(caracteristicas: Pick<ICaracteristicas, 'id'>): number {
    return caracteristicas.id;
  }

  compareCaracteristicas(o1: Pick<ICaracteristicas, 'id'> | null, o2: Pick<ICaracteristicas, 'id'> | null): boolean {
    return o1 && o2 ? this.getCaracteristicasIdentifier(o1) === this.getCaracteristicasIdentifier(o2) : o1 === o2;
  }

  addCaracteristicasToCollectionIfMissing<Type extends Pick<ICaracteristicas, 'id'>>(
    caracteristicasCollection: Type[],
    ...caracteristicasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const caracteristicas: Type[] = caracteristicasToCheck.filter(isPresent);
    if (caracteristicas.length > 0) {
      const caracteristicasCollectionIdentifiers = caracteristicasCollection.map(
        caracteristicasItem => this.getCaracteristicasIdentifier(caracteristicasItem)!
      );
      const caracteristicasToAdd = caracteristicas.filter(caracteristicasItem => {
        const caracteristicasIdentifier = this.getCaracteristicasIdentifier(caracteristicasItem);
        if (caracteristicasCollectionIdentifiers.includes(caracteristicasIdentifier)) {
          return false;
        }
        caracteristicasCollectionIdentifiers.push(caracteristicasIdentifier);
        return true;
      });
      return [...caracteristicasToAdd, ...caracteristicasCollection];
    }
    return caracteristicasCollection;
  }
}
