import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CaracteristicasComponent } from './list/caracteristicas.component';
import { CaracteristicasDetailComponent } from './detail/caracteristicas-detail.component';
import { CaracteristicasUpdateComponent } from './update/caracteristicas-update.component';
import { CaracteristicasDeleteDialogComponent } from './delete/caracteristicas-delete-dialog.component';
import { CaracteristicasRoutingModule } from './route/caracteristicas-routing.module';

@NgModule({
  imports: [SharedModule, CaracteristicasRoutingModule],
  declarations: [
    CaracteristicasComponent,
    CaracteristicasDetailComponent,
    CaracteristicasUpdateComponent,
    CaracteristicasDeleteDialogComponent,
  ],
})
export class CaracteristicasModule {}
