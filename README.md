# 🍽️ Restobar - Sistema de Gestión

Aplicación de escritorio desarrollada en JavaFX para la gestión integral de un restobar: control de usuarios con roles, productos del menú, mesas y pedidos. Proyecto final de la asignatura Programación Orientada a Objetos.

## 📋 Descripción del proyecto

El sistema permite administrar las operaciones diarias de un restobar mediante tres roles de usuario (Administrador, Cajero y Reportes), cada uno con acceso a distintas funcionalidades del dashboard. Incluye gestión de productos (platos y bebidas), control de mesas, registro de pedidos y consultas de reportes, todo respaldado por una base de datos relacional.

## 👥 Integrantes

- Victor Stalin — usuario GitHub: Victorstalin64
- Kevin Mendoza — usuario GitHub: kevinmendoza2001

## 🛠️ Herramientas y tecnologías usadas

- Java (JDK 17+)
- JavaFX — interfaz gráfica de escritorio
- PostgreSQL — base de datos relacional
- JDBC — conexión entre Java y PostgreSQL
- Maven — gestión de dependencias
- pgAdmin — administración de la base de datos
- IntelliJ IDEA — entorno de desarrollo
- Git & GitHub — control de versiones

## 📦 Arquitectura del proyecto

```
ProyectoPOO/
 ├── src/main/java/
 │   ├── app/         (Main.java)
 │   ├── model/       (Persona, Usuario, ClaseTematica)
 │   ├── dao/         (ICRUD, UsuarioDAO, RecursoDAO)
 │   ├── controller/  (LoginController, DashboardController, RecursoController)
 │   └── db/          (Conexion.java)
 ├── src/main/resources/
 │   ├── view/        (login.fxml, dashboard.fxml, recurso.fxml)
 │   ├── css/         (estilos.css)
 │   └── database/    (script.sql)
 └── pom.xml
```

## 🗄️ Base de datos

Motor: PostgreSQL. El script de creación se encuentra en `database/script.sql` y contiene las tablas principales: `usuarios`, `productos`, `mesas`, `pedidos` y `detalle_pedido`, con sus respectivas relaciones (llaves foráneas).

## ⚙️ Pasos de instalación

1. Clonar el repositorio
   ```
   git clone https://github.com/kevinmendoza2001/ProyectoPOO.git
   ```

2. Abrir el proyecto en IntelliJ IDEA
   - File → Open y seleccionar la carpeta clonada.
   - Confiar en el proyecto (Trust Project) cuando lo solicite.

3. Crear la base de datos en pgAdmin
   - Crear una base llamada `restobar_db`.
   - Ejecutar el script `database/script.sql` dentro de esa base (Query Tool).

4. Configurar la conexión
   - En `db/Conexion.java`, ajustar usuario y contraseña de tu PostgreSQL local:
   ```java
   private static final String URL = "jdbc:postgresql://localhost:5432/restobar_db";
   private static final String USUARIO = "postgres";
   private static final String PASSWORD = "tu_password";
   ```

## ✅ Estado actual del proyecto (Avance 1)

- [x] Repositorio GitHub creado
- [x] Primer commit realizado
- [x] Estructura de paquetes creada
- [x] Clase abstracta e interfaz implementadas
- [x] Modelo de clases terminado
- [x] Base de datos creada con tablas principales
- [x] Script SQL inicial
