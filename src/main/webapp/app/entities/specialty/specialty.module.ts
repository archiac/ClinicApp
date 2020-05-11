import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { ClinicSharedModule } from 'app/shared/shared.module';
import { SpecialtyComponent } from './specialty.component';
import { SpecialtyDetailComponent } from './specialty-detail.component';
import { specialtyRoute } from './specialty.route';

@NgModule({
  imports: [ClinicSharedModule, RouterModule.forChild(specialtyRoute)],
  declarations: [SpecialtyComponent, SpecialtyDetailComponent]
})
export class ClinicSpecialtyModule {}
