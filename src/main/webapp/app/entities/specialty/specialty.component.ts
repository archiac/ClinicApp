import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ISpecialty } from 'app/shared/model/specialty.model';
import { SpecialtyService } from './specialty.service';

@Component({
  selector: 'jhi-specialty',
  templateUrl: './specialty.component.html'
})
export class SpecialtyComponent implements OnInit, OnDestroy {
  specialties?: ISpecialty[];
  eventSubscriber?: Subscription;

  constructor(protected specialtyService: SpecialtyService, protected eventManager: JhiEventManager) {}

  loadAll(): void {
    this.specialtyService.query().subscribe((res: HttpResponse<ISpecialty[]>) => (this.specialties = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSpecialties();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISpecialty): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSpecialties(): void {
    this.eventSubscriber = this.eventManager.subscribe('specialtyListModification', () => this.loadAll());
  }
}
