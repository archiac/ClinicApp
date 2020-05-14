import { Component, OnInit } from '@angular/core';
import { IDoctor } from 'app/shared/model/doctor.model';
import { DoctorService } from 'app/entities/doctor/doctor.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

@Component({
  selector: 'jhi-addticket',
  templateUrl: './addticket.component.html',
  styleUrls: ['./addticket.component.scss']
})
export class AddticketComponent implements OnInit {
  doctor?: IDoctor;

  constructor(protected doctorService: DoctorService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  ngOnInit(): void {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  addticket(id: number): void {}
}
