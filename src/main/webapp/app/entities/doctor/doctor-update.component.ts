import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IDoctor, Doctor } from 'app/shared/model/doctor.model';
import { DoctorService } from './doctor.service';
import { ISpecialty } from 'app/shared/model/specialty.model';
import { SpecialtyService } from 'app/entities/specialty/specialty.service';

@Component({
  selector: 'jhi-doctor-update',
  templateUrl: './doctor-update.component.html'
})
export class DoctorUpdateComponent implements OnInit {
  isSaving = false;
  specialties: ISpecialty[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    surname: [],
    patronymic: [],
    phone: [],
    tickets: [],
    specialtyId: []
  });

  constructor(
    protected doctorService: DoctorService,
    protected specialtyService: SpecialtyService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ doctor }) => {
      this.updateForm(doctor);

      this.specialtyService.query().subscribe((res: HttpResponse<ISpecialty[]>) => (this.specialties = res.body || []));
    });
  }

  updateForm(doctor: IDoctor): void {
    this.editForm.patchValue({
      id: doctor.id,
      name: doctor.name,
      surname: doctor.surname,
      patronymic: doctor.patronymic,
      phone: doctor.phone,
      tickets: doctor.tickets,
      specialtyId: doctor.specialtyId
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const doctor = this.createFromForm();
    if (doctor.id !== undefined) {
      this.subscribeToSaveResponse(this.doctorService.update(doctor));
    } else {
      this.subscribeToSaveResponse(this.doctorService.create(doctor));
    }
  }

  private createFromForm(): IDoctor {
    return {
      ...new Doctor(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      surname: this.editForm.get(['surname'])!.value,
      patronymic: this.editForm.get(['patronymic'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      tickets: this.editForm.get(['tickets'])!.value,
      specialtyId: this.editForm.get(['specialtyId'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDoctor>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: ISpecialty): any {
    return item.id;
  }
}
