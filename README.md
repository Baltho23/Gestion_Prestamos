# Sistema de Gestión de Préstamos Bancarios

Prueba Técnica Makers — Backend **Spring Boot 4 / Java 21** + Frontend **Angular 20**.

Un usuario solicita un préstamo (monto + plazo), un administrador lo aprueba o rechaza,
y el usuario consulta el estado de sus solicitudes.

---

## Contenido del repositorio

| Carpeta / archivo | Qué es |
|---|---|
| `sistemabancario/` | API REST. Arquitectura hexagonal, Spring Data JPA, Spring Security + JWT, caché EHCache, tests JUnit 5. |
| `sistemaBancarioFront/` | SPA Angular. Standalone components, Reactive Forms, Guards, interceptor JWT, Tailwind CSS. |
| `TEORICA.md` | Respuestas de la parte teórica de la prueba. |

---

## Requisitos previos

| Herramienta | Versión | Nota |
|---|---|---|
| **JDK** | 21 | Si no lo tienes, el wrapper de Gradle descarga el toolchain automáticamente. |
| **Node.js + npm** | Node 18+ | Para el frontend. |
| **Base de datos** | — | **No hace falta.** El backend arranca con **H2 en memoria**. |

No hay que instalar Gradle ni Angular CLI: se usan los binarios incluidos en cada proyecto.

---

## Puesta en marcha

Son dos procesos, en dos terminales. Arranca primero el backend.

### 1. Backend  →  http://localhost:8080

```bash
cd sistemabancario

./gradlew bootRun            # Linux / macOS / Git Bash
# gradlew.bat bootRun        # Windows (CMD)
# .\gradlew bootRun          # Windows (PowerShell)
```

Al arrancar:
- Usa **H2 en memoria**. Hibernate genera el esquema.
- Carga **5 usuarios de prueba** desde `src/main/resources/data.sql`.
- No hay que crear ninguna base de datos ni ejecutar SQL.

Está listo cuando ves `Started SistemabancarioApplication`.

> Los datos viven en memoria: cada reinicio del backend vuelve al estado semilla.

### 2. Frontend  →  http://localhost:4200

```bash
cd sistemaBancarioFront

npm install
npm start
```

Abre `http://localhost:4200`. El SPA apunta a `http://localhost:8080/api` y el backend
solo acepta CORS desde `http://localhost:4200`.

### 3. Probar la aplicación

1. Inicia sesión como **usuario** (`ana.torres@gmail.com` / `123456`).
2. En *Solicitar Préstamo* introduce un monto y un plazo, y envía. Aparece en *Mis Préstamos* como **PENDIENTE**.
3. Cierra sesión e inicia como **administrador** (`laura.martinez@bancolineal.com` / `123456`).
4. En *Gestionar Solicitudes de Préstamos* pulsa **Aprobar** o **Rechazar** en esa solicitud.
5. Vuelve a entrar como el usuario: el préstamo ahora figura como **APROBADO** / **RECHAZADO**,
   con el nombre del administrador que lo resolvió.

---

## Credenciales de prueba

Todos los usuarios comparten la contraseña **`123456`**.

| Rol | Email | Nombre |
|---|---|---|
| **ADMIN** | `laura.martinez@bancolineal.com` | Laura Martínez |
| **ADMIN** | `carlos.ramirez@bancolineal.com` | Carlos Ramírez |
| **USER** | `ana.torres@gmail.com` | Ana Torres |
| **USER** | `diego.fernandez@gmail.com` | Diego Fernández |
| **USER** | `maria.gonzalez@outlook.com` | María González |
