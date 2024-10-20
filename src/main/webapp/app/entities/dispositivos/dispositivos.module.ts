import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DispositivosComponent } from './list/dispositivos.component';
import { DispositivosDetailComponent } from './detail/dispositivos-detail.component';
import { DispositivosUpdateComponent } from './update/dispositivos-update.component';
import { DispositivosDeleteDialogComponent } from './delete/dispositivos-delete-dialog.component';
import { DispositivosRoutingModule } from './route/dispositivos-routing.module';

@NgModule({
  imports: [SharedModule, DispositivosRoutingModule],
  declarations: [DispositivosComponent, DispositivosDetailComponent, DispositivosUpdateComponent, DispositivosDeleteDialogComponent],
})
export class DispositivosModule {}
