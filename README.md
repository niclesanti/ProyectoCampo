# 📈💰 Sistema de Gestión de Finanzas Personales

## 📖 Descripción General
**Campito** es una aplicación web desarrollada con **Spring Boot** y **Java** que fue pensada para gestionar mis ingresos/gastos de dinero personales, como también gestionar, de manera simple y eficiente, las finanzas de mi grupo familiar que son productores ganaderos a pequeña escala.
El sistema centraliza información sobre **transacciones, presupuestos, espacios de trabajo y control de gastos/ingresos**, ofreciendo a los usuarios un **dashboard interactivo** para el análisis de datos.

Este proyecto fue diseñado con un enfoque en **buenas prácticas de ingeniería de software** y aplicando todos los conceptos, conocimientos y técnicas aprendidos durante el curso de la carrera de Ingeniería en Sistemas de Información.

---

## 🎯 Objetivos del Sistema
- Poder crear diferentes **espacios de trabajo** y compartirlos con otros usuarios.
- Facilitar la **gestión de ingresos y gastos** por espacio de trabajo.
- Permitir la **autenticación segura** mediante Google OAuth 2.0.
- Proveer un **panel de control (dashboard)** con visualizaciones claras sobre:
  - Comparativa mensual de ingresos vs gastos.
  - Distribución porcentual de gastos por motivo.
  - Tendencia del saldo acumulado.
- Asegurar **portabilidad** para entornos de desarrollo y producción.

---

## 🛠️ Tecnologías Utilizadas

![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-25A162?style=for-the-badge&logo=junit5&logoColor=white)
![Google Cloud](https://img.shields.io/badge/Google_Cloud-4285F4?style=for-the-badge&logo=google-cloud&logoColor=white)
![Google OAuth 2.0](https://img.shields.io/badge/Google_OAuth-4285F4?style=for-the-badge&logo=google&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![Chart.js](https://img.shields.io/badge/Chart.js-FF6384?style=for-the-badge&logo=chartdotjs&logoColor=white)
![PgAdmin](https://img.shields.io/badge/PgAdmin-336791?style=for-the-badge&logo=postgresql&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2_Database-007396?style=for-the-badge&logo=databricks&logoColor=white)
![Mockito](https://img.shields.io/badge/Mockito-FFCA28?style=for-the-badge&logo=java&logoColor=black)

### Backend
- **Java 21**
- **Spring Framework** - **Spring Boot**
- **Spring Data JPA** + **Hibernate**
- **PostgreSQL 14** como base de datos relacional.
- **Maven** para gestión de dependencias.
- **JUnit 5** + **Mockito** para pruebas unitarias.
- **Docker** para despliegue y contenedorización.
- **Google OAuth 2.0** para autenticación.

### Frontend (integrado en el backend)
- **HTML5**, **CSS3**, **JavaScript**
- **Chart.js** (librería utilizada para gráficos)
- Integrado en **templates de Spring Boot**.

### Infraestructura y despliegue
- **Google Cloud Run** (producción)
- **Docker Compose** (desarrollo)
- **Neon.tech** / **PostgreSQL en la nube** (Base de datos en producción)
- **PgAdmin** para administración de base de datos.

---

## 🗂️ Arquitectura del Proyecto
Se pensó en una arquitectura simple por el hecho de sería un sistema pequeño de baja concurrencia.
El proyecto sigue una **arquitectura en capas** con separación de responsabilidades clara:

- **Model:** Clases de negocio y entidades JPA.
- **Repository:** Interfaces que extienden `JpaRepository` para acceso a datos.
- **Service:** Contiene la lógica de negocio e interacción con repositorios.
- **Controller:** Define los endpoints REST y gestiona las solicitudes HTTP.
- **Frontend:** Archivos HTML, CSS y JS dentro de `src/main/resources/static`.

---

## 📊 Funcionalidades Principales

1. **Gestión de Usuarios**
   - Registro e inicio de sesión mediante Google OAuth 2.0.

2. **Gestión de Espacios de Trabajo**
   - Creación de diferentes espacios de trabajo (cada uno es independiente del otro).
   - Compartir espacios de trabajo entre usuarios registrados en el sistema para colaborar.

3. **Movimientos de dinero**
   - Registro de ingresos y gastos de dinero en diferentes espacios de trabajo.
   - A cada movimiento de dinero se le asigna una fecha, motivo, monto, emisor/destinatario, cuenta bancaria, etc.

5. **Dashboard**
   - Ingresos vs gastos mensuales.
   - Distribución porcentual de gastos por motivo.
   - Evolución del saldo acumulado.

---

## 🚀 Despliegue

### Desarrollo local
Requiere tener instalado:
- **Docker** y **Docker Compose**
- **Java 21**

Pasos:
```bash
# Clonar el repositorio
git clone git@github.com:niclesanti/ProyectoCampo.git
cd [repositorio]

# ----
# Crear un archivo .env con las variables de entorno en la raiz del proyecto

# Configuración de la base de datos PostgreSQL
DB_NAME=campito_db
DB_USER=campito_user
DB_PASSWORD=campito_pass
DB_HOST=db
DB_PORT=5432

# Configuración de pgAdmin
PGADMIN_EMAIL=admin@campito.com
PGADMIN_PASSWORD=admin123

# Configuración de Spring Boot (opcional)
SPRING_PROFILES_ACTIVE=dev

# Configuración de Google OAuth2
GOOGLE_CLIENT_ID=[Pegar un id de cliente de google]
GOOGLE_CLIENT_SECRET=[Pegar un secreto de cliente de google]
# ----

# Construye la imagen y levantar servicios (app + bdd + pgadmin)
docker-compose up --build

````

La aplicación estará disponible en:
[http://localhost:8080](http://localhost:8080)

---

### Producción (Google Cloud Run)

La aplicación está desplegada en:
**\[URL de producción]** *(https://campito-665598202897.us-central1.run.app/)*

Base de datos alojada en: **Neon.tech**.

---

## 🧪 Pruebas

El proyecto incluye pruebas unitarias con:

* **JUnit 5** para la lógica de negocio.
* **Mockito** para simulación de dependencias.
* **Base de datos H2** en memoria para pruebas de repositorios.

Ejecutar pruebas:

```bash
.\mvnw test
```

---

## 📄 Documentación
A continuación se listan documentos del proyecto:

*   [Diagrama de Clases UML](./docs/DiagramaDeClasesUML.puml)
*   [Historias de Usuario](./docs/HistoriasDeUsuario.md)
*   [Planificación de Pruebas](./docs/PlanificacionPruebas.md)
*   [Problemas y Soluciones](./docs/ProblemasSoluciones.md)

---

## 📈 Mejoras Futuras

* Agregar nuevas funcionalidades para gestión de la producción de ganado.
  * Se podría llevar a una refactorización para dividir la arquitectura monolítica en 3 microservicios:
    * Gestión de usuarios.
    * Gestión de finanzas.
    * Gestión de producción.
  * Separar el frontend en un proyecto independiente (React o Angular).
* Autenticación con otros proveedores (Facebook, Microsoft, etc.).
* Sistema de notificaciones automáticas.
* Implementación de mensajería asíncrona (RabbitMQ o Kafka).

Este proyecto se encuentra en un proceso de mejora continua a medida que surgen nuevas necesidades y desafíos.

---

## 👨‍💻 Autor

* **Nombre:** Nicle Santiago
* **Email:** niclesantiago@gmail.com
* 🔗 [LinkedIn](https://www.linkedin.com/in/santiago-nicle/)
* 🔗 [GitHub](https://github.com/niclesanti)


