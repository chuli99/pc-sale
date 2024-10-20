import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAdicionales } from '../adicionales.model';
import { AdicionalesService } from '../service/adicionales.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './adicionales-delete-dialog.component.html',
})
export class AdicionalesDeleteDialogComponent {
  adicionales?: IAdicionales;

  constructor(protected adicionalesService: AdicionalesService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.adicionalesService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
