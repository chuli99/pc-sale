import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'dispositivos',
        data: { pageTitle: 'tiendaApp.dispositivos.home.title' },
        loadChildren: () => import('./dispositivos/dispositivos.module').then(m => m.DispositivosModule),
      },
      {
        path: 'caracteristicas',
        data: { pageTitle: 'tiendaApp.caracteristicas.home.title' },
        loadChildren: () => import('./caracteristicas/caracteristicas.module').then(m => m.CaracteristicasModule),
      },
      {
        path: 'personalizaciones',
        data: { pageTitle: 'tiendaApp.personalizaciones.home.title' },
        loadChildren: () => import('./personalizaciones/personalizaciones.module').then(m => m.PersonalizacionesModule),
      },
      {
        path: 'opciones',
        data: { pageTitle: 'tiendaApp.opciones.home.title' },
        loadChildren: () => import('./opciones/opciones.module').then(m => m.OpcionesModule),
      },
      {
        path: 'adicionales',
        data: { pageTitle: 'tiendaApp.adicionales.home.title' },
        loadChildren: () => import('./adicionales/adicionales.module').then(m => m.AdicionalesModule),
      },
      {
        path: 'ventas',
        data: { pageTitle: 'tiendaApp.ventas.home.title' },
        loadChildren: () => import('./ventas/ventas.module').then(m => m.VentasModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
