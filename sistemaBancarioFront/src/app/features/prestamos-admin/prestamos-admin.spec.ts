import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { PrestamosAdmin } from './prestamos-admin';

describe('PrestamosAdmin', () => {
  let component: PrestamosAdmin;
  let fixture: ComponentFixture<PrestamosAdmin>;
  let http: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PrestamosAdmin],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(PrestamosAdmin);
    component = fixture.componentInstance;
    http = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
    http.expectOne((r) => r.url.endsWith('/prestamos')).flush([]);
  });

  afterEach(() => http.verify());

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('ordena las solicitudes pendientes primero', () => {
    const pendiente = { id: 1, usuarioId: 1, solicitanteNombre: 'Usuario', resueltoPorId: null, resueltoPorNombre: null, monto: 100, plazoMeses: 6, estado: 'PENDIENTE', fechaSolicitud: '', fechaResolucion: null };
    const aprobado = { id: 2, usuarioId: 1, solicitanteNombre: 'Usuario', resueltoPorId: 2, resueltoPorNombre: 'Admin', monto: 100, plazoMeses: 6, estado: 'APROBADO', fechaSolicitud: '', fechaResolucion: '' };
    component['refrescar']();
    http.expectOne((r) => r.url.endsWith('/prestamos')).flush([aprobado, pendiente]);
    expect(component['lista']()[0].id).toBe(1);
  });
});
