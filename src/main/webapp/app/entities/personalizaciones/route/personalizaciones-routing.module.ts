import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonalizacionesComponent } from '../list/personalizaciones.component';
import { PersonalizacionesDetailComponent } from '../detail/personalizaciones-detail.component';
import { PersonalizacionesUpdateComponent } from '../update/personalizaciones-update.component';
import { PersonalizacionesRoutingResolveService } from './personalizaciones-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const personalizacionesRoute: Routes = [
  {
    path: '',
    component: PersonalizacionesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonalizacionesDetailComponent,
    resolve: {
      personalizaciones: PersonalizacionesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonalizacionesUpdateComponent,
    resolve: {
      personalizaciones: PersonalizacionesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonalizacionesUpdateComponent,
    resolve: {
      personalizaciones: PersonalizacionesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personalizacionesRoute)],
  exports: [RouterModule],
})
export class PersonalizacionesRoutingModule {}
