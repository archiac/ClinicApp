import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClinicSharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { ClinicDoctorModule } from 'app/entities/doctor/doctor.module';

@NgModule({
  imports: [ClinicSharedModule, RouterModule.forChild([HOME_ROUTE]), ClinicDoctorModule],
  declarations: [HomeComponent]
})
export class ClinicHomeModule {}
