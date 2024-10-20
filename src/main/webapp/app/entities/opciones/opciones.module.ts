import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OpcionesComponent } from './list/opciones.component';
import { OpcionesDetailComponent } from './detail/opciones-detail.component';
import { OpcionesUpdateComponent } from './update/opciones-update.component';
import { OpcionesDeleteDialogComponent } from './delete/opciones-delete-dialog.component';
import { OpcionesRoutingModule } from './route/opciones-routing.module';

@NgModule({
  imports: [SharedModule, OpcionesRoutingModule],
  declarations: [OpcionesComponent, OpcionesDetailComponent, OpcionesUpdateComponent, OpcionesDeleteDialogComponent],
})
export class OpcionesModule {}
