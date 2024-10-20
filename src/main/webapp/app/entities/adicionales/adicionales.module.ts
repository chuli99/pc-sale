import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AdicionalesComponent } from './list/adicionales.component';
import { AdicionalesDetailComponent } from './detail/adicionales-detail.component';
import { AdicionalesUpdateComponent } from './update/adicionales-update.component';
import { AdicionalesDeleteDialogComponent } from './delete/adicionales-delete-dialog.component';
import { AdicionalesRoutingModule } from './route/adicionales-routing.module';

@NgModule({
  imports: [SharedModule, AdicionalesRoutingModule],
  declarations: [AdicionalesComponent, AdicionalesDetailComponent, AdicionalesUpdateComponent, AdicionalesDeleteDialogComponent],
})
export class AdicionalesModule {}
