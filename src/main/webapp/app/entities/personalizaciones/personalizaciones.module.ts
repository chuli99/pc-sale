import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PersonalizacionesComponent } from './list/personalizaciones.component';
import { PersonalizacionesDetailComponent } from './detail/personalizaciones-detail.component';
import { PersonalizacionesUpdateComponent } from './update/personalizaciones-update.component';
import { PersonalizacionesDeleteDialogComponent } from './delete/personalizaciones-delete-dialog.component';
import { PersonalizacionesRoutingModule } from './route/personalizaciones-routing.module';

@NgModule({
  imports: [SharedModule, PersonalizacionesRoutingModule],
  declarations: [
    PersonalizacionesComponent,
    PersonalizacionesDetailComponent,
    PersonalizacionesUpdateComponent,
    PersonalizacionesDeleteDialogComponent,
  ],
})
export class PersonalizacionesModule {}
