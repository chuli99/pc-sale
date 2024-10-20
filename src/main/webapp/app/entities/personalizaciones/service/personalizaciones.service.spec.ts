import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPersonalizaciones } from '../personalizaciones.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../personalizaciones.test-samples';

import { PersonalizacionesService } from './personalizaciones.service';

const requireRestSample: IPersonalizaciones = {
  ...sampleWithRequiredData,
};

describe('Personalizaciones Service', () => {
  let service: PersonalizacionesService;
  let httpMock: HttpTestingController;
  let expectedResult: IPersonalizaciones | IPersonalizaciones[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PersonalizacionesService);
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

    it('should create a Personalizaciones', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const personalizaciones = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(personalizaciones).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Personalizaciones', () => {
      const personalizaciones = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(personalizaciones).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Personalizaciones', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Personalizaciones', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Personalizaciones', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPersonalizacionesToCollectionIfMissing', () => {
      it('should add a Personalizaciones to an empty array', () => {
        const personalizaciones: IPersonalizaciones = sampleWithRequiredData;
        expectedResult = service.addPersonalizacionesToCollectionIfMissing([], personalizaciones);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personalizaciones);
      });

      it('should not add a Personalizaciones to an array that contains it', () => {
        const personalizaciones: IPersonalizaciones = sampleWithRequiredData;
        const personalizacionesCollection: IPersonalizaciones[] = [
          {
            ...personalizaciones,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPersonalizacionesToCollectionIfMissing(personalizacionesCollection, personalizaciones);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Personalizaciones to an array that doesn't contain it", () => {
        const personalizaciones: IPersonalizaciones = sampleWithRequiredData;
        const personalizacionesCollection: IPersonalizaciones[] = [sampleWithPartialData];
        expectedResult = service.addPersonalizacionesToCollectionIfMissing(personalizacionesCollection, personalizaciones);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personalizaciones);
      });

      it('should add only unique Personalizaciones to an array', () => {
        const personalizacionesArray: IPersonalizaciones[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const personalizacionesCollection: IPersonalizaciones[] = [sampleWithRequiredData];
        expectedResult = service.addPersonalizacionesToCollectionIfMissing(personalizacionesCollection, ...personalizacionesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const personalizaciones: IPersonalizaciones = sampleWithRequiredData;
        const personalizaciones2: IPersonalizaciones = sampleWithPartialData;
        expectedResult = service.addPersonalizacionesToCollectionIfMissing([], personalizaciones, personalizaciones2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(personalizaciones);
        expect(expectedResult).toContain(personalizaciones2);
      });

      it('should accept null and undefined values', () => {
        const personalizaciones: IPersonalizaciones = sampleWithRequiredData;
        expectedResult = service.addPersonalizacionesToCollectionIfMissing([], null, personalizaciones, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(personalizaciones);
      });

      it('should return initial array if no Personalizaciones is added', () => {
        const personalizacionesCollection: IPersonalizaciones[] = [sampleWithRequiredData];
        expectedResult = service.addPersonalizacionesToCollectionIfMissing(personalizacionesCollection, undefined, null);
        expect(expectedResult).toEqual(personalizacionesCollection);
      });
    });

    describe('comparePersonalizaciones', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePersonalizaciones(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePersonalizaciones(entity1, entity2);
        const compareResult2 = service.comparePersonalizaciones(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePersonalizaciones(entity1, entity2);
        const compareResult2 = service.comparePersonalizaciones(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePersonalizaciones(entity1, entity2);
        const compareResult2 = service.comparePersonalizaciones(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
