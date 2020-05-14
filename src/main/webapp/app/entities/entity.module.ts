import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AddticketComponent } from './addticket/addticket.component';
import { NgJhipsterModule } from 'ng-jhipster';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'doctor',
        loadChildren: () => import('./doctor/doctor.module').then(m => m.ClinicDoctorModule)
      },
      {
        path: 'specialty',
        loadChildren: () => import('./specialty/specialty.module').then(m => m.ClinicSpecialtyModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
    NgJhipsterModule
  ],
  declarations: [AddticketComponent]
})
export class ClinicEntityModule {}
