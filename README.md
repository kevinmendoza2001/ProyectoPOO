# 🍽️ Restobar - Sistema de Gestión

Aplicación de escritorio desarrollada en JavaFX para la gestión integral de un restobar: control de usuarios con roles, productos del menú, ventas, reportes y configuración del negocio. Proyecto final desarrollado para la asignatura de **Programación Orientada a Objetos (POO)**.

---

## 📋 Descripción del proyecto

El sistema permite administrar las operaciones diarias de un restobar mediante tres roles de usuario (**Administrador**, **Cajero** y **Reportes**), cada uno con acceso a distintas funcionalidades y un dashboard personalizado. Incluye gestión completa de productos (con control automático de stock), registro de ventas con generación de comprobantes, exportación de reportes en formato CSV y configuración general del establecimiento, todo respaldado por una base de datos relacional PostgreSQL.

---

## 👥 Integrantes / Autores

- **Victor Stalin** — GitHub: [@Victorstalin64](https://github.com/Victorstalin64)
- **Kevin Mendoza** — GitHub: [@kevinmendoza2001](https://github.com/kevinmendoza2001)

---

## 🚀 Funcionalidades principales

- **Seguridad & Acceso:**
  - Inicio de sesión con autenticación de credenciales.
  - Interfaz gráfica con personalización de temas según el rol del usuario.
- **Gestión de Productos e Inventario:**
  - Registro, edición y eliminación de productos (platos y bebidas).
  - Consulta en tiempo real del stock disponible.
- **Módulo de Ventas & Facturación:**
  - Registro de ventas y cálculo automático del total a pagar.
  - Actualización automática del stock de productos al completar una venta.
  - Generación e impresión/exportación del comprobante de venta.
- **Reportes & Analítica:**
  - Consulta y descarga de reportes de inventario y ventas en formato CSV.
- **Administración & Configuración:**
  - Gestión completa de usuarios y asignación de roles.
  - Configuración de datos institucionales/comerciales de la empresa.

---

## 🔐 Control de Roles y Permisos

| Rol | Permisos y Acceso |
|---|---|
| **Administrador** | Acceso total: Productos, Usuarios, Ventas, Reportes y Configuración de la empresa |
| **Cajero** | Registro de ventas y consulta de stock de productos |
| **Reportes** | Consulta y descarga de reportes e inventario |

---

## 🛠️ Herramientas y Tecnologías Usadas

- **Lenguaje:** Java (JDK 21)
- **Interfaz Gráfica:** JavaFX 21 & FXML
- **Estilos:** CSS3 para la personalización de la UI
- **Base de Datos:** PostgreSQL
- **Conectividad:** JDBC (Java Database Connectivity)
- **Gestor de Dependencias:** Apache Maven
- **Entorno de Desarrollo (IDE):** IntelliJ IDEA
- **Control de Versiones:** Git & GitHub

---

## 📦 Arquitectura del Proyecto

```text
ProyectoPOO/
 ├── src/
 │    └── main/
 │         ├── java/
 │         │    ├── app/          # Clase principal (Main.java)
 │         │    ├── controller/   # Controladores JavaFX (LoginController, DashboardController, etc.)
 │         │    ├── dao/          # Capa de acceso a datos (ICRUD, UsuarioDAO, ProductoDAO, etc.)
 │         │    ├── db/           # Configuración de conexión a PostgreSQL (Conexion.java)
 │         │    └── model/        # Clases del modelo de dominio (Usuario, Producto, Venta, etc.)
 │         └── resources/
 │              ├── css/          # Hojas de estilo visuales (.css)
 │              ├── database/     # Script SQL de la base de datos (script.sql)
 │              └── view/         # Interfaces de usuario en FXML (login.fxml, dashboard.fxml, etc.)
 └── pom.xml                      # Archivo de configuración Maven

🗄️ Base de Datos
Motor: PostgreSQL.

El script de creación de la base de datos se encuentra en src/main/resources/database/script.sql e incluye las tablas principales: usuarios, productos, mesas, pedidos/ventas y detalle_pedido, junto con sus respectivas relaciones e integridad referencial (llaves foráneas).

⚙️ Configuración e Instalación
1. Requisitos Previos
JDK 21 o superior instalado y configurado.

Maven 3.8+

PostgreSQL 14+ y pgAdmin.

IntelliJ IDEA (recomendado).

2. Clonar el Repositorio
Bash
git clone [https://github.com/kevinmendoza2001/ProyectoPOO.git](https://github.com/kevinmendoza2001/ProyectoPOO.git)
cd ProyectoPOO
3. Configurar la Base de Datos
Abre pgAdmin o tu cliente PostgreSQL preferido.

Crea una base de datos nombrada restobar_db.

Abre la herramienta de consultas (Query Tool) y ejecuta el script ubicado en:
src/main/resources/database/script.sql

4. Configurar Credenciales de Conexión
Edita el archivo src/main/java/db/Conexion.java con las credenciales de tu servidor PostgreSQL locaL o en la nube
