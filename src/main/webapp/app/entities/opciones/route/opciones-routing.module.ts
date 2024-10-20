import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OpcionesComponent } from '../list/opciones.component';
import { OpcionesDetailComponent } from '../detail/opciones-detail.component';
import { OpcionesUpdateComponent } from '../update/opciones-update.component';
import { OpcionesRoutingResolveService } from './opciones-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const opcionesRoute: Routes = [
  {
    path: '',
    component: OpcionesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OpcionesDetailComponent,
    resolve: {
      opciones: OpcionesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OpcionesUpdateComponent,
    resolve: {
      opciones: OpcionesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OpcionesUpdateComponent,
    resolve: {
      opciones: OpcionesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(opcionesRoute)],
  exports: [RouterModule],
})
export class OpcionesRoutingModule {}
