# FoodStore - Backend

Backend del sistema e-commerce FoodStore. Aplicación Java con JPA/Hibernate que proporciona la lógica de negocio y persistencia de datos.

---

**Parte del TPI:** Programación III (UTN)  
**Video presentación:** [Link del video](#)

---

### Características principales

- **Gestión de categorías:** CRUD de categorías de productos
- **Gestión de productos:** CRUD con validación de stock y disponibilidad
- **Gestión de usuarios:** Alta, baja, modificación de usuarios con roles
- **Sistema de pedidos:** Creación de pedidos atómicos con validación de stock
- **Detalles de pedido:** Relación 1:N entre pedidos y productos
- **Reportes JPQL:** Productos por categoría, pedidos por usuario/estado, totales facturados
- **Menú interactivo:** Interfaz de consola para todas las operaciones

### Tecnologías usadas

- **[Java](https://www.java.com/es/)** 21 - Lenguaje de programación
- **[JPA/Hibernate](https://hibernate.org/)** - ORM para persistencia
- **[H2 Database](https://www.h2database.com/)** - Base de datos en archivo
- **[Gradle](https://gradle.org/)** - Gestor de dependencias y build

### Requisitos previos

- **[Java](https://www.java.com/es/)** JDK 21 o superior

### Instalación y ejecución

``` Terminal
# Compilar el proyecto
gradle build

# Ejecutar la aplicación
gradle run
```

---

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
