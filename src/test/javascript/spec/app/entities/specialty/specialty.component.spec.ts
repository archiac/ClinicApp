import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ClinicTestModule } from '../../../test.module';
import { SpecialtyComponent } from 'app/entities/specialty/specialty.component';
import { SpecialtyService } from 'app/entities/specialty/specialty.service';
import { Specialty } from 'app/shared/model/specialty.model';

describe('Component Tests', () => {
  describe('Specialty Management Component', () => {
    let comp: SpecialtyComponent;
    let fixture: ComponentFixture<SpecialtyComponent>;
    let service: SpecialtyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ClinicTestModule],
        declarations: [SpecialtyComponent]
      })
        .overrideTemplate(SpecialtyComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SpecialtyComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SpecialtyService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Specialty(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.specialties && comp.specialties[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
