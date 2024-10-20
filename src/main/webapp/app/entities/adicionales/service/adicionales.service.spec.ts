import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAdicionales } from '../adicionales.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../adicionales.test-samples';

import { AdicionalesService } from './adicionales.service';

const requireRestSample: IAdicionales = {
  ...sampleWithRequiredData,
};

describe('Adicionales Service', () => {
  let service: AdicionalesService;
  let httpMock: HttpTestingController;
  let expectedResult: IAdicionales | IAdicionales[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AdicionalesService);
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

    it('should create a Adicionales', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const adicionales = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(adicionales).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Adicionales', () => {
      const adicionales = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(adicionales).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Adicionales', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Adicionales', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Adicionales', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAdicionalesToCollectionIfMissing', () => {
      it('should add a Adicionales to an empty array', () => {
        const adicionales: IAdicionales = sampleWithRequiredData;
        expectedResult = service.addAdicionalesToCollectionIfMissing([], adicionales);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(adicionales);
      });

      it('should not add a Adicionales to an array that contains it', () => {
        const adicionales: IAdicionales = sampleWithRequiredData;
        const adicionalesCollection: IAdicionales[] = [
          {
            ...adicionales,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAdicionalesToCollectionIfMissing(adicionalesCollection, adicionales);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Adicionales to an array that doesn't contain it", () => {
        const adicionales: IAdicionales = sampleWithRequiredData;
        const adicionalesCollection: IAdicionales[] = [sampleWithPartialData];
        expectedResult = service.addAdicionalesToCollectionIfMissing(adicionalesCollection, adicionales);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(adicionales);
      });

      it('should add only unique Adicionales to an array', () => {
        const adicionalesArray: IAdicionales[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const adicionalesCollection: IAdicionales[] = [sampleWithRequiredData];
        expectedResult = service.addAdicionalesToCollectionIfMissing(adicionalesCollection, ...adicionalesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const adicionales: IAdicionales = sampleWithRequiredData;
        const adicionales2: IAdicionales = sampleWithPartialData;
        expectedResult = service.addAdicionalesToCollectionIfMissing([], adicionales, adicionales2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(adicionales);
        expect(expectedResult).toContain(adicionales2);
      });

      it('should accept null and undefined values', () => {
        const adicionales: IAdicionales = sampleWithRequiredData;
        expectedResult = service.addAdicionalesToCollectionIfMissing([], null, adicionales, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(adicionales);
      });

      it('should return initial array if no Adicionales is added', () => {
        const adicionalesCollection: IAdicionales[] = [sampleWithRequiredData];
        expectedResult = service.addAdicionalesToCollectionIfMissing(adicionalesCollection, undefined, null);
        expect(expectedResult).toEqual(adicionalesCollection);
      });
    });

    describe('compareAdicionales', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAdicionales(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAdicionales(entity1, entity2);
        const compareResult2 = service.compareAdicionales(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAdicionales(entity1, entity2);
        const compareResult2 = service.compareAdicionales(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAdicionales(entity1, entity2);
        const compareResult2 = service.compareAdicionales(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
