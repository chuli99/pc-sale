import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICaracteristicas } from '../caracteristicas.model';
import { CaracteristicasService } from '../service/caracteristicas.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './caracteristicas-delete-dialog.component.html',
})
export class CaracteristicasDeleteDialogComponent {
  caracteristicas?: ICaracteristicas;

  constructor(protected caracteristicasService: CaracteristicasService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.caracteristicasService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
