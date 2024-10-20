import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonalizaciones } from '../personalizaciones.model';
import { PersonalizacionesService } from '../service/personalizaciones.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './personalizaciones-delete-dialog.component.html',
})
export class PersonalizacionesDeleteDialogComponent {
  personalizaciones?: IPersonalizaciones;

  constructor(protected personalizacionesService: PersonalizacionesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personalizacionesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
