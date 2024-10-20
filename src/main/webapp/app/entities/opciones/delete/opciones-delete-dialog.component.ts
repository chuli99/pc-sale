import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOpciones } from '../opciones.model';
import { OpcionesService } from '../service/opciones.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './opciones-delete-dialog.component.html',
})
export class OpcionesDeleteDialogComponent {
  opciones?: IOpciones;

  constructor(protected opcionesService: OpcionesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.opcionesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
