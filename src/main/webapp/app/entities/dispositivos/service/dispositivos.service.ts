import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDispositivos, NewDispositivos } from '../dispositivos.model';

export type PartialUpdateDispositivos = Partial<IDispositivos> & Pick<IDispositivos, 'id'>;

export type EntityResponseType = HttpResponse<IDispositivos>;
export type EntityArrayResponseType = HttpResponse<IDispositivos[]>;

@Injectable({ providedIn: 'root' })
export class DispositivosService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dispositivos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dispositivos: NewDispositivos): Observable<EntityResponseType> {
    return this.http.post<IDispositivos>(this.resourceUrl, dispositivos, { observe: 'response' });
  }

  update(dispositivos: IDispositivos): Observable<EntityResponseType> {
    return this.http.put<IDispositivos>(`${this.resourceUrl}/${this.getDispositivosIdentifier(dispositivos)}`, dispositivos, {
      observe: 'response',
    });
  }

  partialUpdate(dispositivos: PartialUpdateDispositivos): Observable<EntityResponseType> {
    return this.http.patch<IDispositivos>(`${this.resourceUrl}/${this.getDispositivosIdentifier(dispositivos)}`, dispositivos, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDispositivos>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDispositivos[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDispositivosIdentifier(dispositivos: Pick<IDispositivos, 'id'>): number {
    return dispositivos.id;
  }

  compareDispositivos(o1: Pick<IDispositivos, 'id'> | null, o2: Pick<IDispositivos, 'id'> | null): boolean {
    return o1 && o2 ? this.getDispositivosIdentifier(o1) === this.getDispositivosIdentifier(o2) : o1 === o2;
  }

  addDispositivosToCollectionIfMissing<Type extends Pick<IDispositivos, 'id'>>(
    dispositivosCollection: Type[],
    ...dispositivosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const dispositivos: Type[] = dispositivosToCheck.filter(isPresent);
    if (dispositivos.length > 0) {
      const dispositivosCollectionIdentifiers = dispositivosCollection.map(
        dispositivosItem => this.getDispositivosIdentifier(dispositivosItem)!
      );
      const dispositivosToAdd = dispositivos.filter(dispositivosItem => {
        const dispositivosIdentifier = this.getDispositivosIdentifier(dispositivosItem);
        if (dispositivosCollectionIdentifiers.includes(dispositivosIdentifier)) {
          return false;
        }
        dispositivosCollectionIdentifiers.push(dispositivosIdentifier);
        return true;
      });
      return [...dispositivosToAdd, ...dispositivosCollection];
    }
    return dispositivosCollection;
  }
}
