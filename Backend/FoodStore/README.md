# FoodStore - Backend

Backend del sistema e-commerce FoodStore. Aplicación Java con JPA/Hibernate que proporciona la lógica de negocio y persistencia de datos.

-----

**Parte del TPI:** Programación III (UTN)  
**Video presentación:** [Link del video](https://youtu.be/8VshIO0qmSs?si=Zqzw4WgiY8RvOSkM)
-----

### Características principales

- **Gestión de categorías:** CRUD de categorías de productos
- **Gestión de productos:** CRUD con validación de stock y disponibilidad
- **Gestión de usuarios:** Alta, baja, modificación de usuarios con roles
- **Sistema de pedidos:** Creación de pedidos atómicos con validación de stock
- **Detalles de pedido:** Relación 1:N entre pedidos y productos
- **Reportes JPQL:** Productos por categoría, pedidos por usuario/estado, totales facturados
- **Menú interactivo:** Interfaz de consola para todas las operaciones
-----

### Tecnologías usadas

- **[Java JDK 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)** - Lenguaje de programación
- **[JPA/Hibernate](https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-core)** 6.5.0 - ORM para persistencia
- **[H2 Database](https://mvnrepository.com/artifact/com.h2database/h2)** 2.2.224 - Base de datos en archivo
- **[Lombok](https://mvnrepository.com/artifact/org.projectlombok/lombok)** 1.18.30 - Generador de código
- **[SLF4J](https://mvnrepository.com/artifact/org.slf4j/slf4j-simple)** 2.0.13 - Logging
-----

### Requisitos previos

- **[Java JDK 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)** o superior

### Instalación y ejecución

```bash
# Limpiar build anterior
.\gradlew clean

# Compilar el proyecto
.\gradlew build

# Ejecutar la aplicación
.\gradlew run
```

# O desde IntelliJ:
# Click derecho en Main.java → Run
-----

## Estructura del Programa

```
src/main/java/com/tp/jpa/
├── exceptions/          # (CantidadInvalidaException, PrecioNoValidoException, ProductoNoDisponibleException, ProductoNoValidoException, StockInsuficienteException)
├── init/               # (DataLoader)
├── menus/              # (CategoriaMenu, PedidoMenu, ProductoMenu, ReportesMenu, UsuarioMenu)
├── model/              # (Base, Calculable, Categoria, DetallePedido, Pedido, Producto, Usuario, DTOs, Enums)
├── repository/         # (BaseRepository, CategoriaRepository, PedidoRepository, ProductoRepository, UsuarioRepository)
└── util/              # (JPAUtil, Main)
```
-----

### Funcionalidades Detalladas

#### Gestión de Categorías
- Alta
- Modificación
- Baja lógica
- Listado de categorías activas

#### Gestión de Productos
- Alta, Modificación, Baja lógica
- Listado de productos
- Asociación con categorías
- Control de stock y disponibilidad

#### Gestión de Usuarios
- Alta, Modificación, Baja lógica
- Listado de usuarios
- Búsqueda por correo electrónico

#### Gestión de Pedidos
- Creación, Modificación de estado, Baja lógica
- Consulta, Búsqueda por usuario y estado
- Descuento automático de stock

#### Reportes
- Productos por categoría
- Pedidos por usuario/estado
- Total facturado
