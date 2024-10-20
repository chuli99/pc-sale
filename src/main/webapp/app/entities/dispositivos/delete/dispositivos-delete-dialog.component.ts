import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDispositivos } from '../dispositivos.model';
import { DispositivosService } from '../service/dispositivos.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './dispositivos-delete-dialog.component.html',
})
export class DispositivosDeleteDialogComponent {
  dispositivos?: IDispositivos;

  constructor(protected dispositivosService: DispositivosService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dispositivosService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
