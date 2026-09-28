# SpeedFast - Gestión de Entregas y Pedidos (Semana 7)

Aplicación desarrollada en Java con interfaz gráfica (Swing) y persistencia en una base de datos relacional MySQL mediante JDBC.

## Descripción del Proyecto

El sistema SpeedFast permite administrar y persistir las operaciones de pedidos, repartidores y entregas en tiempo real. La solución implementa el patrón de arquitectura por capas (Modelo, DAO, Vista, Conexión), garantizando la separación de responsabilidades, el manejo seguro de datos mediante sentencias preparadas (`PreparedStatement`) y el control ordenado de recursos con bloques `try-catch-finally`.

## Estructura del Proyecto

src/
├── conexion/
│   └── ConexionBD.java          # Conexión JDBC a MySQL y cierre de recursos
├── dao/
│   ├── EntregaDAO.java          # Persistencia y asignación de entregas
│   ├── PedidoDAO.java           # Registro y listado de pedidos
│   └── RepartidorDAO.java       # Registro y listado de repartidores
├── modelo/
│   ├── Entrega.java             # Entidad Entrega
│   ├── Pedido.java              # Entidad Pedido
│   └── Repartidor.java          # Entidad Repartidor
└── vista/
└── VentanaPrincipal.java    # Interfaz gráfica Swing y visualización con JTable

## Características Implementadas

- Gestión de Base de Datos: Esquema relacional `speedfast_db` con llaves foráneas (`FOREIGN KEY`) e integridad referencial en cascada.
- Conectividad JDBC: Configuración centralizada en la clase `ConexionBD` utilizando el driver `mysql-connector-j`.
- Patrón DAO (Data Access Object):
    - `PedidoDAO`: Inserción segura con `PreparedStatement` y consulta global (`listarTodos`).
    - `RepartidorDAO`: Inserción y consulta devolviendo colecciones `List<Repartidor>` mediante `ResultSet`.
    - `EntregaDAO`: Inserción de la asignación y relación entre pedidos y repartidores.
- Interfaz Gráfica (GUI): Formularios para captura de información y componente `JTable` sincronizado en tiempo real.

## Modelo de Base de Datos (MySQL)

CREATE DATABASE IF NOT EXISTS speedfast_db;
USE speedfast_db;

CREATE TABLE IF NOT EXISTS repartidor (
id INT AUTO_INCREMENT PRIMARY KEY,
nombre VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS pedido (
id INT AUTO_INCREMENT PRIMARY KEY,
direccion VARCHAR(150) NOT NULL,
tipo VARCHAR(30) NOT NULL,
estado VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS entrega (
id INT AUTO_INCREMENT PRIMARY KEY,
id_pedido INT NOT NULL,
id_repartidor INT NOT NULL,
fecha DATE NOT NULL,
hora TIME NOT NULL,
FOREIGN KEY (id_pedido) REFERENCES pedido(id) ON DELETE CASCADE,
FOREIGN KEY (id_repartidor) REFERENCES repartidor(id) ON DELETE CASCADE
);

## Requisitos y Configuración de Ejecución

1. Java Development Kit (JDK): Versión 17 o superior (desarrollado y probado en JDK 23).
2. Servidor MySQL: Servidor local activo en el puerto 3306 (mediante XAMPP).
3. Dependencias: Archivo JAR del driver `mysql-connector-j` agregado a las librerías del proyecto.
4. Instrucciones de Ejecución:
    - Iniciar el servicio MySQL desde el panel de XAMPP.
    - Ejecutar el script SQL provisto para crear la base de datos `speedfast_db` y sus tablas.
    - Ejecutar la clase principal `vista.VentanaPrincipal` desde IntelliJ IDEA para interactuar con la aplicación.