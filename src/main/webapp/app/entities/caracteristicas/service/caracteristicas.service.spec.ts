import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICaracteristicas } from '../caracteristicas.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../caracteristicas.test-samples';

import { CaracteristicasService } from './caracteristicas.service';

const requireRestSample: ICaracteristicas = {
  ...sampleWithRequiredData,
};

describe('Caracteristicas Service', () => {
  let service: CaracteristicasService;
  let httpMock: HttpTestingController;
  let expectedResult: ICaracteristicas | ICaracteristicas[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CaracteristicasService);
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

    it('should create a Caracteristicas', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const caracteristicas = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(caracteristicas).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Caracteristicas', () => {
      const caracteristicas = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(caracteristicas).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Caracteristicas', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Caracteristicas', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Caracteristicas', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCaracteristicasToCollectionIfMissing', () => {
      it('should add a Caracteristicas to an empty array', () => {
        const caracteristicas: ICaracteristicas = sampleWithRequiredData;
        expectedResult = service.addCaracteristicasToCollectionIfMissing([], caracteristicas);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(caracteristicas);
      });

      it('should not add a Caracteristicas to an array that contains it', () => {
        const caracteristicas: ICaracteristicas = sampleWithRequiredData;
        const caracteristicasCollection: ICaracteristicas[] = [
          {
            ...caracteristicas,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCaracteristicasToCollectionIfMissing(caracteristicasCollection, caracteristicas);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Caracteristicas to an array that doesn't contain it", () => {
        const caracteristicas: ICaracteristicas = sampleWithRequiredData;
        const caracteristicasCollection: ICaracteristicas[] = [sampleWithPartialData];
        expectedResult = service.addCaracteristicasToCollectionIfMissing(caracteristicasCollection, caracteristicas);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(caracteristicas);
      });

      it('should add only unique Caracteristicas to an array', () => {
        const caracteristicasArray: ICaracteristicas[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const caracteristicasCollection: ICaracteristicas[] = [sampleWithRequiredData];
        expectedResult = service.addCaracteristicasToCollectionIfMissing(caracteristicasCollection, ...caracteristicasArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const caracteristicas: ICaracteristicas = sampleWithRequiredData;
        const caracteristicas2: ICaracteristicas = sampleWithPartialData;
        expectedResult = service.addCaracteristicasToCollectionIfMissing([], caracteristicas, caracteristicas2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(caracteristicas);
        expect(expectedResult).toContain(caracteristicas2);
      });

      it('should accept null and undefined values', () => {
        const caracteristicas: ICaracteristicas = sampleWithRequiredData;
        expectedResult = service.addCaracteristicasToCollectionIfMissing([], null, caracteristicas, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(caracteristicas);
      });

      it('should return initial array if no Caracteristicas is added', () => {
        const caracteristicasCollection: ICaracteristicas[] = [sampleWithRequiredData];
        expectedResult = service.addCaracteristicasToCollectionIfMissing(caracteristicasCollection, undefined, null);
        expect(expectedResult).toEqual(caracteristicasCollection);
      });
    });

    describe('compareCaracteristicas', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCaracteristicas(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCaracteristicas(entity1, entity2);
        const compareResult2 = service.compareCaracteristicas(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCaracteristicas(entity1, entity2);
        const compareResult2 = service.compareCaracteristicas(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCaracteristicas(entity1, entity2);
        const compareResult2 = service.compareCaracteristicas(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
