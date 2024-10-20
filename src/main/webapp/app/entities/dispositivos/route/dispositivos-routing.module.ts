import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DispositivosComponent } from '../list/dispositivos.component';
import { DispositivosDetailComponent } from '../detail/dispositivos-detail.component';
import { DispositivosUpdateComponent } from '../update/dispositivos-update.component';
import { DispositivosRoutingResolveService } from './dispositivos-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const dispositivosRoute: Routes = [
  {
    path: '',
    component: DispositivosComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DispositivosDetailComponent,
    resolve: {
      dispositivos: DispositivosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DispositivosUpdateComponent,
    resolve: {
      dispositivos: DispositivosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DispositivosUpdateComponent,
    resolve: {
      dispositivos: DispositivosRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dispositivosRoute)],
  exports: [RouterModule],
})
export class DispositivosRoutingModule {}
