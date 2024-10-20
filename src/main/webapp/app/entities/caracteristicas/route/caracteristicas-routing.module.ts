import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CaracteristicasComponent } from '../list/caracteristicas.component';
import { CaracteristicasDetailComponent } from '../detail/caracteristicas-detail.component';
import { CaracteristicasUpdateComponent } from '../update/caracteristicas-update.component';
import { CaracteristicasRoutingResolveService } from './caracteristicas-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const caracteristicasRoute: Routes = [
  {
    path: '',
    component: CaracteristicasComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CaracteristicasDetailComponent,
    resolve: {
      caracteristicas: CaracteristicasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CaracteristicasUpdateComponent,
    resolve: {
      caracteristicas: CaracteristicasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CaracteristicasUpdateComponent,
    resolve: {
      caracteristicas: CaracteristicasRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(caracteristicasRoute)],
  exports: [RouterModule],
})
export class CaracteristicasRoutingModule {}
