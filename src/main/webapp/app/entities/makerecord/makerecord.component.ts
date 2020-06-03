import { Component, OnInit } from '@angular/core';
import { IDoctor } from 'app/shared/model/doctor.model';
import { DoctorService } from 'app/entities/doctor/doctor.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

@Component({
  selector: 'jhi-makerecord',
  templateUrl: './makerecord.component.html',
  styleUrls: ['./makerecord.component.scss']
})
export class MakerecordComponent implements OnInit {
  doctor?: IDoctor;

  constructor(protected doctorService: DoctorService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  ngOnInit(): void {}

  cancel(): void {
    this.activeModal.dismiss();
  }
}
