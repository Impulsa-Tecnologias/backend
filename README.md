# Cibot-Chat (Backend)

¡Bienvenido al repositorio del backend de **Cibot-Chat**! Esta es una API REST desarrollada con **Spring Boot 4** y **Java 25** que actúa como el motor principal de la aplicación. Se encarga de la gestión de usuarios, flujos de chats, historial de mensajes, persistencia de recetas y la integración con modelos de inteligencia artificial a través de **OpenRouter**.

Este proyecto está diseñado para trabajar en conjunto con el repositorio del Frontend (puedes encontrarlo aquí: [Cibot-Chat Frontend](https://github.com/Impulsa-Tecnologias/frontend)).

---

## 🚀 Tecnologías Principales

* **Java 25** & **Spring Boot 4.0.6**
* **Spring Data JPA** (Persistencia de datos)
* **PostgreSQL** (Base de datos relacional)
* **Spring Security & JWT** (Autenticación y autorización segura)
* **OpenRouter API** (Integración con modelos de lenguaje/IA)
* **Lombok** (Reducción de código repetitivo)
* **Springdoc OpenAPI / Swagger UI** (Documentación de la API)

---

## 📋 Requisitos Previos

Antes de clonar e iniciar el proyecto, asegúrate de tener instalado lo siguiente en tu entorno local:

1.  **Java Development Kit (JDK) 25**
2.  **Apache Maven** (o usar el wrapper `./mvnw` incluido)
3.  **PostgreSQL** (Servicio activo local o remotamente)
4.  Una cuenta y una API Key en [OpenRouter](https://openrouter.ai/) (opcional para desarrollo local, pero necesaria para las funciones de IA).

---

## ⚙️ Instalación y Configuración

Sigue estos pasos para levantar el entorno de desarrollo:

### 1. Clonar el repositorio
Abre una terminal y ejecuta el siguiente comando:
```bash
git clone https://github.com/Impulsa-Tecnologias/backend.git
cd backend
```
### 2. Configurar las variables de entorno (.env)
El proyecto utiliza variables de entorno para proteger los datos sensibles y las credenciales. En la raíz del proyecto encontrarás un archivo llamado `.env.example`.
#### 1. Copia el archivo y renombralo a .env
```bash
cp .env.example .env
```
#### 2. Abre el archivo .env recién creado y reemplaza los valores de ejemplo por tus credenciales reales:
```Fragmento de código
# Conexión a la Base de Datos PostgreSQL
DATABASE_URL=jdbc:postgresql://localhost:5432/tu_base_de_datos
DATABASE_USER=tu_usuario_postgres
DATABASE_PASSWORD=tu_contraseña_postgres

# Clave de Seguridad JWT (Debe tener un mínimo de 32 caracteres para ser segura)
JWT_SECRET_KEY=coloca_aqui_una_clave_secreta_muy_larga_y_segura_de_mas_de_32_caracteres

# API Key de OpenRouter
OPENROUTER_API_KEY=tu_api_key_real_de_openrouter
```
⚠️ Nota: El archivo `.env` real está incluido en el `.gitignore`, por lo que tus credenciales locales nunca se subirán a GitHub.

### 3. Construir y ejecutar la aplicación
Una vez configurada la base de datos y el archivo `.env`, compila y ejecuta el proyecto con Maven:
```bash
# En sistemas basados en Linux/macOS:
./mvnw spring-boot:run

# En Windows (CMD / PowerShell):
.\mvnw.cmd spring-boot:run
```
El servidor web se iniciará por defecto en el puerto `8082` (puedes configurarlo en `application.properties`).

---

## 📖 Documentación de la API (Swagger)
Una vez que el backend esté corriendo, puedes acceder a la interfaz interactiva de Swagger para probar los endpoints de usuarios, mensajería y recetas ingresando a:

Swagger UI:`http://localhost:8080/swagger-ui/index.html`
