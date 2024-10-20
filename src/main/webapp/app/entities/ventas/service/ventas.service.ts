import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVentas, NewVentas } from '../ventas.model';

export type PartialUpdateVentas = Partial<IVentas> & Pick<IVentas, 'id'>;

type RestOf<T extends IVentas | NewVentas> = Omit<T, 'fechaVenta'> & {
  fechaVenta?: string | null;
};

export type RestVentas = RestOf<IVentas>;

export type NewRestVentas = RestOf<NewVentas>;

export type PartialUpdateRestVentas = RestOf<PartialUpdateVentas>;

export type EntityResponseType = HttpResponse<IVentas>;
export type EntityArrayResponseType = HttpResponse<IVentas[]>;

@Injectable({ providedIn: 'root' })
export class VentasService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ventas');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ventas: NewVentas): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ventas);
    return this.http
      .post<RestVentas>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ventas: IVentas): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ventas);
    return this.http
      .put<RestVentas>(`${this.resourceUrl}/${this.getVentasIdentifier(ventas)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ventas: PartialUpdateVentas): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ventas);
    return this.http
      .patch<RestVentas>(`${this.resourceUrl}/${this.getVentasIdentifier(ventas)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestVentas>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestVentas[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getVentasIdentifier(ventas: Pick<IVentas, 'id'>): number {
    return ventas.id;
  }

  compareVentas(o1: Pick<IVentas, 'id'> | null, o2: Pick<IVentas, 'id'> | null): boolean {
    return o1 && o2 ? this.getVentasIdentifier(o1) === this.getVentasIdentifier(o2) : o1 === o2;
  }

  addVentasToCollectionIfMissing<Type extends Pick<IVentas, 'id'>>(
    ventasCollection: Type[],
    ...ventasToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ventas: Type[] = ventasToCheck.filter(isPresent);
    if (ventas.length > 0) {
      const ventasCollectionIdentifiers = ventasCollection.map(ventasItem => this.getVentasIdentifier(ventasItem)!);
      const ventasToAdd = ventas.filter(ventasItem => {
        const ventasIdentifier = this.getVentasIdentifier(ventasItem);
        if (ventasCollectionIdentifiers.includes(ventasIdentifier)) {
          return false;
        }
        ventasCollectionIdentifiers.push(ventasIdentifier);
        return true;
      });
      return [...ventasToAdd, ...ventasCollection];
    }
    return ventasCollection;
  }

  protected convertDateFromClient<T extends IVentas | NewVentas | PartialUpdateVentas>(ventas: T): RestOf<T> {
    return {
      ...ventas,
      fechaVenta: ventas.fechaVenta?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restVentas: RestVentas): IVentas {
    return {
      ...restVentas,
      fechaVenta: restVentas.fechaVenta ? dayjs(restVentas.fechaVenta) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestVentas>): HttpResponse<IVentas> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestVentas[]>): HttpResponse<IVentas[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
