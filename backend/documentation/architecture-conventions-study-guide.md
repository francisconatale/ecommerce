# Guía de Arquitectura y Convenciones del Proyecto

Este documento está diseñado como una **guía amigable y orientada a humanos** para entender cómo está estructurado el backend de nuestra aplicación. No solo explica *dónde* va cada cosa, sino también *por qué* y *qué conceptos* puedes estudiar para dominar esta arquitectura.

---

## 1. La Arquitectura Hexagonal (Puertos y Adaptadores)

El proyecto sigue una estructura estricta basada en **Domain-Driven Design (DDD)** y **Arquitectura Hexagonal**. El objetivo principal de esto es **proteger las reglas de tu negocio** de los detalles técnicos (como qué base de datos usas o si tu API es REST o GraphQL).

### 📁 `domain` (El Corazón de la Aplicación)
Aquí viven tus reglas de negocio reales. 
* **Regla de oro:** ¡Cero dependencias externas! No verás importaciones de Spring Framework, Jakarta Persistence (JPA) o Jackson (JSON) aquí. Si Spring Boot desaparece mañana, tu carpeta `domain` debería seguir compilando intacta.
* **Contiene:** Modelos (Ej. `Product`), Servicios (Ej. `ProductService`), Excepciones de negocio y las **Interfaces (Puertos)** de repositorios que dicen *qué* necesitamos guardar, pero no *cómo*.

### 📁 `infrastructure/web` (La Capa de Presentación)
Es la puerta de entrada a tu aplicación desde el exterior.
* **Responsabilidad:** Recibir peticiones HTTP (REST), validar los datos de entrada, llamar a los servicios del dominio, y devolver respuestas (JSON) al cliente.
* **Contiene:** Controladores, DTOs (Data Transfer Objects como `ProductRequest` / `ProductResponse`), manejo global de errores (`GlobalExceptionHandler`), y clases de formato (`ApiResponse`).

### 📁 `infrastructure/persistence` (La Capa de Datos)
Es la puerta de salida hacia tu base de datos.
* **Responsabilidad:** Guardar y recuperar datos de Postgres/MySQL. 
* **Contiene:** Entidades JPA (`ProductEntity`), Repositorios de Spring Data, **Adaptadores** (que implementan las interfaces del dominio usando código de base de datos) y **Mappers** (para transformar entre entidades JPA y modelos del dominio).

---

## 2. Temas de Estudio Sugeridos para Profundizar

Si quieres volverte un experto en la estructura y herramientas de este proyecto, aquí tienes los temas clave que debes investigar:

### 🔍 1. Serialización y Deserialización en Java (Jackson)
En la capa web, transformamos objetos Java en JSON y viceversa.
* **Conceptos a estudiar:** 
  * ¿Cómo funciona Jackson bajo el capó?
  * Anotaciones clave: `@JsonProperty`, `@JsonInclude(Include.NON_NULL)`, `@JsonIgnore`.
  * *Type Erasure* en Java y cómo Jackson serializa objetos genéricos (como `ApiResponse<List<Product>>`).
  * Deserialización de `Records` de Java (introducidos en Java 14+ y cómo Spring Boot 3 / Jackson 2.12+ los maneja nativamente).

### 🔍 2. Mappers (ej. MapStruct)
En la capa de persistencia, evitamos que los objetos de base de datos (`ProductEntity`) viajen al `domain` (`Product`). Para no copiar campo por campo manualmente (ej. `producto.setNombre(entidad.getNombre())`), usamos Mapeadores.
* **Conceptos a estudiar:**
  * El patrón **DTO** (Data Transfer Object) vs **Domain Model**.
  * Herramientas de generación de código en compilación como **MapStruct**.
  * ¿Cómo configurar un mapper para que ignore campos nulos o transforme tipos (ej. de `String` a `UUID`)?

### 🔍 3. Jakarta EE (Persistencia / JPA / Hibernate)
El estándar de Java para interactuar con bases de datos relacionales es Jakarta Persistence (anteriormente Java EE / JPA), y su implementación más famosa es Hibernate.
* **Conceptos a estudiar:**
  * Diferencia entre JPA (la especificación) e Hibernate (la implementación).
  * Anotaciones de mapeo relacional: `@Entity`, `@Table`, `@Column`, `@Id`, `@GeneratedValue`.
  * Ciclo de vida de una Entidad (Transient, Managed, Detached, Removed).
  * Relaciones (`@OneToMany`, `@ManyToOne`) y los problemas de rendimiento comunes como el **N+1 Queries Problem**.

### 🔍 4. Spring Core y Spring Web
Es la "magia" que une toda nuestra infraestructura.
* **Conceptos a estudiar:**
  * **Inyección de Dependencias (DI)** e **Inversión de Control (IoC)** (Anotaciones como `@Service`, `@Component`, `@Configuration`, y cómo Spring crea el "ApplicationContext").
  * **Filtros HTTP y DispatcherServlet**: Cómo una petición de Postman llega físicamente a tu método de un `@RestController`.
  * **ControllerAdvice**: El mecanismo detrás de tu `GlobalExceptionHandler` para atrapar errores y devolver códigos HTTP correctos (404, 400, 500) de manera limpia.

### 🔍 5. Arquitectura de Puertos y Adaptadores en la Práctica
* **Conceptos a estudiar:**
  * El principio de Inversión de Dependencias (La 'D' en SOLID).
  * ¿Por qué el dominio define una interfaz `ProductRepository` y la infraestructura crea un `ProductRepositoryAdapter` que implementa esa interfaz? (Esto es literalmente el Puerto y el Adaptador).
