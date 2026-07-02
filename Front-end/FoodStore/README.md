# FoodStore - Frontend

Frontend del sistema e-commerce FoodStore. Aplicación web interactiva desarrollada con TypeScript y Vite.
-----
**Parte del TPI:** Programación III (UTN)  
**Video presentación:** [Link del video](https://youtu.be/A5Y7XRBFGy8?si=eUa_g6GJbj-8YUTn)
-----
### Características principales

- **Catálogo dinámico:** Grid de productos con filtro por categorías y buscador
- **Detalle de producto:** Vista 2-columnas con imagen, descripción, precio y opción de agregar al carrito
- **Carrito de compras:** Gestión de items (aumentar/disminuir), resumen de totales, checkout con modal
- **Autenticación:** Login y registro de usuarios con persistencia en localStorage
- **Mis Pedidos:** Historial de pedidos del usuario autenticado
- **Panel Administrativo:** Dashboard con estadísticas, tablas read-only de categorías/productos/pedidos
- **Protección de rutas:** Redireccionamiento según rol (ADMIN/USUARIO)
-----

### Tecnologías usadas

- **[Node.js](https://nodejs.org/es)** v18 o superior
- **[pnpm](https://pnpm.io/es/)** como gestor de paquetes
- **HTML5 + CSS3** - Maquetación y estilos
- **localStorage** - Persistencia de datos
-----

### Instalación y ejecución

```Terminal
# Instalar dependencias
pnpm install

# Ejecutar en desarrollo
pnpm run dev o pnpm dev

```
-----

### Credenciales de prueba

**ADMIN:**  
Email: `admin@admin.com`  
Password: `123456`

**USUARIO:**  
Email: `cliente@food.com`  
Password: `cliente123`

-----

## Estructura del Programa

```
src/
├── pages/               # Páginas: auth, store, client, admin
│   ├── auth/           # Login, Registro
│   ├── store/          # Home, ProductDetail, Cart
│   ├── client/         # Orders
│   └── admin/          # Dashboard, Categorías, Productos, Pedidos
├── types/              # IUser, IProducto, ICategoria, IPedido, IDetalle
├── utils/              # auth.ts, dataService.ts, navigate.ts, localStorage.ts
├── assets/             # Imágenes y recursos
├── style.css           # Estilos globales
└── main.ts             # Routing y protección de rutas
```
-----

### Funcionalidades Detalladas

#### Autenticación
- Login con validación de credenciales
- Registro de nuevos usuarios
- Logout y cierre de sesión

#### Catálogo
- Visualizar productos en grid
- Filtrar por categorías
- Buscador de productos

#### Carrito
- Agregar productos al carrito
- Aumentar/Disminuir cantidad
- Eliminar productos
- Calcular subtotal, envío y total

#### Checkout
- Formulario con datos de entrega
- Selección de método de pago
- Confirmación de pedido

#### Mis Pedidos
- Visualizar historial de pedidos
- Ver detalles (productos, total, estado)

#### Panel Admin
- Dashboard con estadísticas
- Visualizar categorías, productos y pedidos
- Protección por rol
