# Recursos sobre Arquitectura de Software y Desacoplamiento

Durante nuestra refactorización de `Category` y `Product` aplicamos principios fundamentales de diseño. Aquí tienes una lista curada de recursos para profundizar en estos conceptos y seguir mejorando tus habilidades de arquitectura:

## 1. Modelado de Dominio (Domain-Driven Design - DDD)

DDD es la filosofía detrás de construir **Modelos de Dominio Ricos**, donde las entidades encapsulan su propia lógica de negocio y validaciones (en lugar de ser simplemente "bolsas de datos" con *getters* y *setters*).

*   **"Domain-Driven Design: Tackling Complexity in the Heart of Software" (Eric Evans)**
    *   **Conocido como:** El "Libro Azul".
    *   **De qué trata:** Es el texto fundacional. Denso y conceptual, introduce todo el lenguaje que hoy usamos en arquitectura de software: *Entities*, *Value Objects*, *Aggregates*, *Repositories* y *Ubiquitous Language* (Lenguaje Ubicuo).
*   **"Implementing Domain-Driven Design" (Vaughn Vernon)**
    *   **Conocido como:** El "Libro Rojo".
    *   **De qué trata:** La bajada a tierra práctica del libro de Evans. Muestra cómo implementar DDD en código real, con ejemplos claros de cómo enriquecer el modelo. Ideal para aplicar al instante.

## 2. Arquitectura Limpia y Desacoplamiento

Estos recursos explican por qué tu lógica de negocio (el centro) no debe depender de bases de datos, frameworks (como Spring Boot) o interfaces de usuario.

*   **"Clean Architecture: A Craftsman's Guide to Software Structure and Design" (Robert C. Martin / Uncle Bob)**
    *   **De qué trata:** Explica la separación de responsabilidades en "capas de cebolla", donde la regla de oro es que las dependencias solo pueden apuntar hacia adentro (hacia el dominio).
*   **Arquitectura Hexagonal (Ports and Adapters)**
    *   **Autor original:** Alistair Cockburn.
    *   **De qué trata:** Este es el patrón que ya estamos aplicando parcialmente en el proyecto (por ejemplo, definir la interfaz `ProductRepository` en el dominio, pero su implementación reside en la capa de persistencia/infraestructura). Busca artículos y charlas introductorias sobre este patrón.

## 3. Calidad de Código y Refactorización

*   **"Refactoring: Improving the Design of Existing Code" (Martin Fowler)**
    *   **De qué trata:** Un catálogo de técnicas (como las que usamos para extraer métodos y mejorar la expresividad de los `if`) para transformar código heredado o acoplado en código limpio, sin romper su funcionalidad.
*   **Blog de Martin Fowler: "Anemic Domain Model"**
    *   **Lectura obligada:** Martin Fowler acuñó el término "Modelo de Dominio Anémico" como un antipatrón. Su artículo corto sobre el tema explica perfectamente por qué empezamos a sacar los *setters* puros y agregamos métodos como `assignToCategory` y validaciones en las entidades.

> [!TIP]
> **Por dónde empezar hoy:** 
> Busca en YouTube charlas sobre **"Rich Domain Models vs Anemic Domain Models"**. Son videos de 30-40 minutos que resumen perfectamente el viaje que hicimos refactorizando las entidades de tu proyecto para hacerlas independientes del framework.
