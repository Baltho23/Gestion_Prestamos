import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';

import { PrestamosUsuario } from './prestamos-usuario';

describe('PrestamosUsuario', () => {
  let component: PrestamosUsuario;
  let fixture: ComponentFixture<PrestamosUsuario>;
  let http: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PrestamosUsuario],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(PrestamosUsuario);
    component = fixture.componentInstance;
    http = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
    http.expectOne((r) => r.url.endsWith('/prestamos/mis')).flush([]);
  });

  afterEach(() => http.verify());

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('no envía si el form es inválido', () => {
    component['solicitar']();
    expect(http.match((r) => r.method === 'POST').length).toBe(0);
  });
});
