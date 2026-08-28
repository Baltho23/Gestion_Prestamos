# Parte Teórica

Prueba Técnica Makers — respuestas a la parte teórica (30%).

---

## 1. Optimización en sistemas financieros

Para la **concurrencia**, una técnica habitual es el bloqueo optimista: cada registro
lleva un número de versión y, si dos operaciones intentan modificar el mismo dato a la
vez, solo una lo consigue y la otra se rechaza en lugar de sobrescribir.

Para el **rendimiento**, se cachean las consultas que se repiten (por ejemplo listados de
estado) y se invalida esa caché en cuanto el dato cambia, para no servir información
desactualizada.

Para **escalar**, conviene que la API no guarde estado de sesión en el servidor
(autenticación por token): así se pueden levantar varias instancias detrás de un
balanceador sin que compartan nada.

---

## 2. Seguridad en APIs financieras

- **Inyección SQL:** se evita usando un ORM o consultas parametrizadas, donde los valores
  nunca se concatenan dentro del texto de la consulta.
- **Autenticación:** tokens firmados con un secreto del servidor y con caducidad, para que
  no se puedan falsificar ni reutilizar indefinidamente. Las contraseñas se guardan con un
  hash lento con sal (BCrypt), nunca en texto plano.
- **Autorización:** se comprueba el rol a nivel de operación, no solo por la URL, y el
  usuario que actúa se identifica siempre por el token, nunca por un id que envíe el
  cliente (evita que alguien acceda a datos de otros).

---

## 4. Pruebas unitarias y de integración

La idea general es tener muchas **pruebas unitarias** —rápidas y aisladas— y pocas
**de integración**, más lentas pero completas.

Las unitarias prueban la lógica de negocio simulando la base de datos (con Mockito), lo
que permite cubrir los casos borde en milisegundos: importes inválidos, registros
inexistentes, operaciones no permitidas por el estado o por el rol.

Las de integración arrancan la aplicación real, normalmente contra una base de datos en
memoria (H2), para comprobar que todas las piezas encajan. Las herramientas habituales en
Spring son JUnit 5, Mockito y AssertJ, incluidas en `spring-boot-starter-test`.

---

## 5. Front-end

El estado de la sesión se centraliza en un servicio con un observable (`BehaviorSubject`),
que notifica a los componentes cuando cambia. NgRx es una opción más potente, pero para
aplicaciones pequeñas suele ser excesivo.

El token se guarda en el navegador y un interceptor lo añade automáticamente a cada
petición; si el servidor responde que el token no es válido, se cierra la sesión. Las
rutas se protegen con guards, teniendo claro que esa protección es solo de experiencia de
usuario: la seguridad real la impone el backend.

Para la coherencia de los datos, tras cada acción conviene volver a consultar al servidor
en lugar de suponer el nuevo estado en el cliente.

---

## 6. Spring Boot

**a. ¿Qué pasa internamente al usar `@SpringBootApplication` y cómo afecta al arranque?**

`@SpringBootApplication` agrupa tres anotaciones: `@Configuration` (permite definir beans),
`@ComponentScan` (registra automáticamente las clases anotadas como `@Service`,
`@RestController`, etc.) y `@EnableAutoConfiguration` (configura lo típico según las
librerías presentes: si detecta JPA y una base de datos, prepara la conexión; si detecta
web, levanta el servidor). Al arrancar, Spring crea todos los beans, los conecta entre sí
e inicia el servidor.

**b. ¿Cómo funciona el ciclo de vida de un bean y cómo intervenir en él?**

Spring crea el bean, le inyecta sus dependencias (preferiblemente por el constructor) y lo
deja listo; al apagar la aplicación lo destruye. Se puede intervenir en ese ciclo con
`@PostConstruct` y `@PreDestroy`, o definiendo el bean a mano en un método `@Bean`.
