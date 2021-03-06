import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClinicSharedModule } from 'app/shared/shared.module';
import { DoctorComponent } from './doctor.component';
import { DoctorDetailComponent } from './doctor-detail.component';
import { DoctorUpdateComponent } from './doctor-update.component';
import { DoctorDeleteDialogComponent } from './doctor-delete-dialog.component';
import { doctorRoute } from './doctor.route';

@NgModule({
  imports: [ClinicSharedModule, RouterModule.forChild(doctorRoute)],
  declarations: [DoctorComponent, DoctorDetailComponent, DoctorUpdateComponent, DoctorDeleteDialogComponent],
  exports: [DoctorComponent],
  entryComponents: [DoctorDeleteDialogComponent]
})
export class ClinicDoctorModule {}
