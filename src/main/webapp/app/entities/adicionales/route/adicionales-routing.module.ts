import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AdicionalesComponent } from '../list/adicionales.component';
import { AdicionalesDetailComponent } from '../detail/adicionales-detail.component';
import { AdicionalesUpdateComponent } from '../update/adicionales-update.component';
import { AdicionalesRoutingResolveService } from './adicionales-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const adicionalesRoute: Routes = [
  {
    path: '',
    component: AdicionalesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AdicionalesDetailComponent,
    resolve: {
      adicionales: AdicionalesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AdicionalesUpdateComponent,
    resolve: {
      adicionales: AdicionalesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AdicionalesUpdateComponent,
    resolve: {
      adicionales: AdicionalesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(adicionalesRoute)],
  exports: [RouterModule],
})
export class AdicionalesRoutingModule {}
