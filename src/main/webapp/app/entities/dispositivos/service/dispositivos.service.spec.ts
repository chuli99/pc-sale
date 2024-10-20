import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDispositivos } from '../dispositivos.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../dispositivos.test-samples';

import { DispositivosService } from './dispositivos.service';

const requireRestSample: IDispositivos = {
  ...sampleWithRequiredData,
};

describe('Dispositivos Service', () => {
  let service: DispositivosService;
  let httpMock: HttpTestingController;
  let expectedResult: IDispositivos | IDispositivos[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DispositivosService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Dispositivos', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const dispositivos = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(dispositivos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Dispositivos', () => {
      const dispositivos = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(dispositivos).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Dispositivos', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Dispositivos', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Dispositivos', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDispositivosToCollectionIfMissing', () => {
      it('should add a Dispositivos to an empty array', () => {
        const dispositivos: IDispositivos = sampleWithRequiredData;
        expectedResult = service.addDispositivosToCollectionIfMissing([], dispositivos);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dispositivos);
      });

      it('should not add a Dispositivos to an array that contains it', () => {
        const dispositivos: IDispositivos = sampleWithRequiredData;
        const dispositivosCollection: IDispositivos[] = [
          {
            ...dispositivos,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDispositivosToCollectionIfMissing(dispositivosCollection, dispositivos);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Dispositivos to an array that doesn't contain it", () => {
        const dispositivos: IDispositivos = sampleWithRequiredData;
        const dispositivosCollection: IDispositivos[] = [sampleWithPartialData];
        expectedResult = service.addDispositivosToCollectionIfMissing(dispositivosCollection, dispositivos);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dispositivos);
      });

      it('should add only unique Dispositivos to an array', () => {
        const dispositivosArray: IDispositivos[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const dispositivosCollection: IDispositivos[] = [sampleWithRequiredData];
        expectedResult = service.addDispositivosToCollectionIfMissing(dispositivosCollection, ...dispositivosArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dispositivos: IDispositivos = sampleWithRequiredData;
        const dispositivos2: IDispositivos = sampleWithPartialData;
        expectedResult = service.addDispositivosToCollectionIfMissing([], dispositivos, dispositivos2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dispositivos);
        expect(expectedResult).toContain(dispositivos2);
      });

      it('should accept null and undefined values', () => {
        const dispositivos: IDispositivos = sampleWithRequiredData;
        expectedResult = service.addDispositivosToCollectionIfMissing([], null, dispositivos, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dispositivos);
      });

      it('should return initial array if no Dispositivos is added', () => {
        const dispositivosCollection: IDispositivos[] = [sampleWithRequiredData];
        expectedResult = service.addDispositivosToCollectionIfMissing(dispositivosCollection, undefined, null);
        expect(expectedResult).toEqual(dispositivosCollection);
      });
    });

    describe('compareDispositivos', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDispositivos(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDispositivos(entity1, entity2);
        const compareResult2 = service.compareDispositivos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDispositivos(entity1, entity2);
        const compareResult2 = service.compareDispositivos(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDispositivos(entity1, entity2);
        const compareResult2 = service.compareDispositivos(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
