import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOpciones } from '../opciones.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../opciones.test-samples';

import { OpcionesService } from './opciones.service';

const requireRestSample: IOpciones = {
  ...sampleWithRequiredData,
};

describe('Opciones Service', () => {
  let service: OpcionesService;
  let httpMock: HttpTestingController;
  let expectedResult: IOpciones | IOpciones[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OpcionesService);
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

    it('should create a Opciones', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const opciones = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(opciones).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Opciones', () => {
      const opciones = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(opciones).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Opciones', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Opciones', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Opciones', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOpcionesToCollectionIfMissing', () => {
      it('should add a Opciones to an empty array', () => {
        const opciones: IOpciones = sampleWithRequiredData;
        expectedResult = service.addOpcionesToCollectionIfMissing([], opciones);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(opciones);
      });

      it('should not add a Opciones to an array that contains it', () => {
        const opciones: IOpciones = sampleWithRequiredData;
        const opcionesCollection: IOpciones[] = [
          {
            ...opciones,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOpcionesToCollectionIfMissing(opcionesCollection, opciones);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Opciones to an array that doesn't contain it", () => {
        const opciones: IOpciones = sampleWithRequiredData;
        const opcionesCollection: IOpciones[] = [sampleWithPartialData];
        expectedResult = service.addOpcionesToCollectionIfMissing(opcionesCollection, opciones);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(opciones);
      });

      it('should add only unique Opciones to an array', () => {
        const opcionesArray: IOpciones[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const opcionesCollection: IOpciones[] = [sampleWithRequiredData];
        expectedResult = service.addOpcionesToCollectionIfMissing(opcionesCollection, ...opcionesArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const opciones: IOpciones = sampleWithRequiredData;
        const opciones2: IOpciones = sampleWithPartialData;
        expectedResult = service.addOpcionesToCollectionIfMissing([], opciones, opciones2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(opciones);
        expect(expectedResult).toContain(opciones2);
      });

      it('should accept null and undefined values', () => {
        const opciones: IOpciones = sampleWithRequiredData;
        expectedResult = service.addOpcionesToCollectionIfMissing([], null, opciones, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(opciones);
      });

      it('should return initial array if no Opciones is added', () => {
        const opcionesCollection: IOpciones[] = [sampleWithRequiredData];
        expectedResult = service.addOpcionesToCollectionIfMissing(opcionesCollection, undefined, null);
        expect(expectedResult).toEqual(opcionesCollection);
      });
    });

    describe('compareOpciones', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOpciones(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOpciones(entity1, entity2);
        const compareResult2 = service.compareOpciones(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOpciones(entity1, entity2);
        const compareResult2 = service.compareOpciones(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOpciones(entity1, entity2);
        const compareResult2 = service.compareOpciones(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
