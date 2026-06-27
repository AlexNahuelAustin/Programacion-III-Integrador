package com.tp.jpa;

import com.tp.jpa.exceptions.PrecioNoValidoException;
import com.tp.jpa.exceptions.StockInsuficienteException;
import com.tp.jpa.init.DataLoader;
import com.tp.jpa.model.Categoria;
import com.tp.jpa.model.Pedido;
import com.tp.jpa.model.Producto;
import com.tp.jpa.model.Usuario;
import com.tp.jpa.model.dtos.*;
import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import com.tp.jpa.model.enums.Rol;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.PedidoRepository;
import com.tp.jpa.repository.ProductoRepository;
import com.tp.jpa.repository.UsuarioRepository;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.*;


public class Main {


    static PedidoRepository pedidoRepository = new PedidoRepository();
    static UsuarioRepository usuarioRepository = new UsuarioRepository();
    static CategoriaRepository categoriaRepository = new CategoriaRepository();
    static ProductoRepository productoRepository = new ProductoRepository();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        DataLoader.cargarDatos(categoriaRepository, productoRepository, usuarioRepository, pedidoRepository);
        menuPrincipal();
        JPAUtil.close();
    }

    private static void menuPrincipal() {
        boolean salir = false;
        while (!salir) {
            System.out.println();
            System.out.println("\n+================================+");
            System.out.println("|   Gestion de pedidos - JPA    |");
            System.out.println("+================================+");
            System.out.println("\n==========================");
            System.out.println("        Menu Principal");
            System.out.println("==========================\n");
            System.out.println("1. Gestionar Categorías");
            System.out.println("2. Gestionar Productos");
            System.out.println("3. Gestionar Usuarios");
            System.out.println("4. Gestionar Pedidos");
            System.out.println("5. Reportes");
            System.out.println("0. Salir");
            System.out.println("----------------------------");
            System.out.print("Seleccione una opcion: ");
            try {
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1 -> subMenuCategoria();
                    case 2 -> subMenuProducto();
                    case 3 -> subMenuUsuario();
                    case 4 -> subMenuPedido();
                    case 5 -> subMenuReportes();
                    case 0 -> salir = true;
                    default -> System.err.println("Numero invalido");
                }
            } catch (NumberFormatException nfe) {
                System.err.println("Error: La opcion debe ser un numero");
            } catch (Exception e) {
                System.err.println("Error al ingresar una opcion!" + e.getMessage());
            }
        }
    }

    // Submenu de categoria
    private static void subMenuCategoria() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- Submenu: Categorias ---");
            System.out.println("1. Alta de categoria");
            System.out.println("2. Modificar categoria");
            System.out.println("3. Baja logica de categoria");
            System.out.println("4. Listar categorias activas");
            System.out.println("0. Volver al menu principal");
            System.out.println("------------------------------");
            System.out.print("Opcion: ");
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> altaCategoria();
                    case 2 -> modificarCategoria();
                    case 3 -> bajaLogicaCategoria();
                    case 4 -> listarCategorias();
                    case 0 -> salir = true;
                    default -> System.err.println("Error: numero invalido");
                }
            } catch (NumberFormatException nfe) {
                System.err.println("Error: La opcion debe ser un numero");
            } catch (Exception e) {
                System.err.println("Error al ingresar una opcion!" + e.getMessage());
            }
        }
    }

    // Submenu de producto
    public static void subMenuProducto() {
        boolean salir = false;
        while (!salir) {
            try {
                System.out.println("\n--- Submenu: Productos ---");
                System.out.println("1. Alta de producto");
                System.out.println("2. Modificar producto");
                System.out.println("3. Baja logica de productos");
                System.out.println("4. Listar productos activas");
                System.out.println("0. Volver al menu principal");
                System.out.println("------------------------------");
                System.out.print("Opcion: ");
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> altaProducto();
                    case 2 -> modificarProducto();
                    case 3 -> bajaLogicaProducto();
                    case 4 -> listarProductos();
                    case 0 -> salir = true;
                    default -> System.out.println("Error: numero invalido");
                }
            } catch (NumberFormatException nfe) {
                System.err.println("Error ingrese un numero valido" + nfe.getMessage());
            } catch (Exception e) {
                System.err.println("Error al ingresar una opcion!" + e.getMessage());
            }
        }
    }

    // Submenu de usuario
    public static void subMenuUsuario() {
        boolean salir = false;
        while (!salir) {
            try {
                System.out.println("\n--- Submenu: Usuario ---");
                System.out.println("1. Alta de usuario");
                System.out.println("2. Modificar usuario");
                System.out.println("3. Baja logica de usuario");
                System.out.println("4. Listar usuarios activos");
                System.out.println("0. Volver al menu principal");
                System.out.println("------------------------------");
                System.out.print("Opcion: ");
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> altaUsuario();
                    case 2 -> modificarUsuario();
                    case 3 -> bajaLogicaUsuario();
                    case 4 -> listarUsuarios();
                    case 5 -> buscarUsuarioPorMail();
                    case 0 -> salir = true;
                    default -> System.out.println("Error: numero invalido");
                }
            } catch (NumberFormatException nfe) {
                System.err.println("Error ingrese un numero valido" + nfe.getMessage());
            } catch (Exception e) {
                System.err.println("Error al ingresar una opcion!" + e.getMessage());
            }
        }
    }

    // Submenu de pedidos
    public static void subMenuPedido() {
        boolean salir = false;
        while (!salir) {
            try {
                System.out.println("\n--- Submenu: Pedido ---");
                System.out.println("1. Alta de pedido");
                System.out.println("2. cambiar estado de pedido");
                System.out.println("3. Baja logica de pedido");
                System.out.println("4. Listar pedidos activas");
                System.out.println("5. Pedidos por usuario");
                System.out.println("6. Pedidos por estado");
                System.out.println("0. Volver al menu principal");
                System.out.println("------------------------------");
                System.out.print("Opcion: ");
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> altaPedido();
                    case 2 -> cambiarEstado();
                    case 3 -> bajaLogicaPedido();
                    case 4 -> listarPedidos();
                    case 5 -> pedidoPorUsuario();
                    case 6 -> pedidoPorEstado();
                    case 0 -> salir = true;
                    default -> System.out.println("Error: numero invalido");
                }
            } catch (NumberFormatException nfe) {
                System.err.println("Error ingrese un numero valido" + nfe.getMessage());
            } catch (Exception e) {
                System.err.println("Error al ingresar una opcion!" + e.getMessage());
            }
        }
    }

    // Submenu de reportes
    private static void subMenuReportes() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- Submenu: Reportes ---");
            System.out.println("1. Productos por categoria");
            System.out.println("2. Pedidos por usuario");
            System.out.println("3. Pedidos por estado");
            System.out.println("4. Total facturado");
            System.out.println("0. Volver al menu principal");
            System.out.println("------------------------------");
            System.out.print("Opcion: ");
            try {
                int opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> mostrarProductosPorCategoria();
                    case 2 -> pedidoPorUsuario();
                    case 3 -> pedidoPorEstado();
                    case 4 -> totalFacturado();
                    case 0 -> salir = true;
                    default -> System.err.println("Error: numero invalido");
                }
            } catch (NumberFormatException nfe) {
                System.err.println("Error: La opcion debe ser un numero");
            } catch (Exception e) {
                System.err.println("Error al ingresar una opcion!" + e.getMessage());
            }
        }
    }


    // Metodos del sub menu de categoria
    public static void altaCategoria() {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();
            if (nombre.isBlank()) {
                System.err.print("Error: El nombre no puede estar vacio");
                return;
            }
            System.out.print("Descripcion: ");
            String descripcion = scanner.nextLine();
            Categoria categoriaNueva = Categoria.builder()
                    .nombre(nombre)
                    .descripcion(descripcion)
                    .build();
            Categoria guardada = categoriaRepository.guardar(categoriaNueva);
            CategoriaDTO categoriaDTO = CategoriaDTO.fromEntidad(guardada);
            System.out.println("Categoria Creada con exito!");
            System.out.println("ID: " + categoriaDTO.id());
        } catch (IllegalArgumentException iae) {
            System.err.println("Error de validacion: " + iae.getMessage());
        } catch (Exception e) {
            System.err.println("Error al crear categoria" + e.getMessage());
        }
    }

    public static void modificarCategoria() {
        listarCategorias();
        try {
            System.out.print("\nIngresa el ID de la categoria a modificar: ");
            Long idABuscar = Long.parseLong(scanner.nextLine());
            categoriaRepository.buscarPorId(idABuscar)
                    .ifPresentOrElse(categoria -> {
                        CategoriaDTO categoriaDTO = CategoriaDTO.fromEntidad(categoria);
                        System.out.println("\n---------- Datos Actuales ----------");
                        System.out.println("Nombre actual: " + categoriaDTO.nombre());
                        System.out.println("Descripcion actual: " + categoriaDTO.descripcion());

                        // Modificar Nombre
                        System.out.print("Nuevo nombre (Enter para mantener): ");
                        String nombreNuevo = scanner.nextLine();
                        if (!nombreNuevo.isBlank()) categoria.setNombre(nombreNuevo);

                        // Modificar descripcion
                        System.out.print("\nNueva descripcion (Enter para mantener): ");
                        String descripcionNueva = scanner.nextLine();
                        if (!descripcionNueva.isBlank()) categoria.setDescripcion(descripcionNueva);

                        categoriaRepository.guardar(categoria);
                        System.out.println("Categoria modificada con exito!");

                    }, () -> System.err.println("Error: no existe categoria con ese ID"));
        } catch (NumberFormatException nfe) {
            System.err.println("Error ingrese un numero valido");
        } catch (Exception e) {
            System.err.println("Error al modificar una categoria: " + e.getMessage());
        }
    }

    public static void bajaLogicaCategoria() {
        listarCategorias();
        System.out.print("Ingresa el ID a de la categoria a eliminar: ");
        try {
            Long idABuscar = Long.parseLong(scanner.nextLine());
            categoriaRepository.buscarPorId(idABuscar)
                    .ifPresentOrElse(categoria -> {
                        CategoriaDTO categoriaDTO = CategoriaDTO.fromEntidad(categoria);
                        System.out.println("-------------------------------");
                        System.out.println("Nombre: " + categoriaDTO.nombre());
                        System.out.println("Descripcion: " + categoriaDTO.descripcion());
                        System.out.println("-------------------------------");
                        System.out.println("¿Está seguro que desea eliminar? ");
                        System.out.print("Ingrese 'si' para confirmar o 'no' para cancelar: ");
                        String opcion = scanner.nextLine();
                        try {

                            Map<String, Runnable> acciones = Map.of(
                                    "si", () -> {
                                        productoRepository.buscarPorCategoria(categoria.getId())
                                                .forEach(producto -> productoRepository.eliminarLogico(producto.getId()));

                                        categoriaRepository.eliminarLogico(categoria.getId());
                                        System.out.println("Categoria " + categoriaDTO.nombre() + " eliminada!");
                                    },
                                    "no", () -> System.out.println("Operacion cancelada")
                            );
                            Optional.ofNullable(acciones.get(opcion.toLowerCase()))
                                    .ifPresentOrElse(Runnable::run,
                                            () -> System.err.println("Opcion invalida"));

                        } catch (IllegalArgumentException iae) {
                            System.err.println("Opcion invalida");
                        } catch (Exception e) {
                            System.err.println("Error al eliminar una categoria: " + e.getMessage());
                        }

                    }, () -> System.out.println("Categoria no encontrado o ya dado de baja"));
        } catch (NumberFormatException nfe) {
            System.err.println("Error ingrese un  valido");
        } catch (Exception e) {
            System.err.println("Error al modificar una categoria: " + e.getMessage());
        }
    }

    public static void listarCategorias() {
        List<Categoria> categorias = categoriaRepository.listarActivos();
        System.out.println("--- Categorias activas ---");
        System.out.printf("%-10s %-20s %20s%n", "ID", "Nombre", "Descripcion");
        System.out.println("-".repeat(85));
        categorias.stream().map(CategoriaDTO::fromEntidad)
                .forEach(categoriaDTO ->
                        System.out.printf("%-10s %-20s %-30s%n",
                                categoriaDTO.id(), categoriaDTO.nombre(), categoriaDTO.descripcion()));

    }

    // Metodos del sub menu de producto
    public static void altaProducto() {
        try {
            System.out.print("\nnombre: ");
            String nombre = scanner.nextLine();
            if (nombre.isBlank()) {
                System.out.println("El nombre no puede estar vacio");
                return;
            }
            System.out.print("\nDescripcion: ");
            String descripcion = scanner.nextLine();

            System.out.print("\nPrecio: ");
            double precio;
            try {
                precio = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("Error: ingrese un numero valido para el precio (ej: 1500.50)");
                return;
            }
            if (precio <= 0) {
                throw new PrecioNoValidoException("El precio debe ser mayor a cero");
            }

            System.out.print("Cantidad inicial de stock: ");
            int stock;
            try {
                stock = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.err.println("Error: ingrese un numero entero valido para el stock");
                return;
            }
            if (stock < 0) {
                throw new StockInsuficienteException("Error: El stock debe cero o mayor");
            }

            listarCategorias();
            List<Categoria> categorias = categoriaRepository.listarActivos();
            if (categorias.isEmpty()) {
                System.out.println("No hay categoria activas");
                return;
            }
            System.out.println("Seleccione ID de categoria");
            Long categoriaId = Long.parseLong(scanner.nextLine());

            Categoria categoria = categoriaRepository.buscarPorId(categoriaId)
                    .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));

            Producto productoNuevo = Producto.builder()
                    .nombre(nombre)
                    .descripcion(descripcion)
                    .precio(precio)
                    .stock(stock)
                    .imagen("Imagen.png")
                    .disponible(true)
                    .build();
            categoria.agregarProductos(productoNuevo);

            Producto guardado = productoRepository.guardar(productoNuevo);
            ProductoDTO productoDTO = ProductoDTO.fromEntidad(guardado);
            System.out.println("Producto Creada con exito!");
            System.out.println("ID: " + productoDTO.id());


        } catch (IllegalArgumentException iae) {
            System.err.println("Error de validacion: " + iae.getMessage());
        } catch (Exception e) {
            System.err.println("Error al crear producto" + e.getMessage());
        }
    }

    public static void modificarProducto() {
        listarProductos();
        try {
            System.out.print("Ingrese el ID del producto a modificar: ");
            Long idABuscar = Long.parseLong(scanner.nextLine());
            productoRepository.buscarPorId(idABuscar)
                    .ifPresentOrElse(producto -> {
                        ProductoDTO productoDTO = ProductoDTO.fromEntidad(producto);
                        System.out.println("\n---------- Datos Actuales ----------");
                        System.out.println("Nombre actual: " + productoDTO.nombre());
                        System.out.println("Descripcion actual: " + productoDTO.descripcion());
                        System.out.println("precio actual: " + productoDTO.precio());
                        System.out.println("Stock actual: " + productoDTO.stock());

                        // Modificar nombre
                        System.out.print("Nuevo nombre (Enter para mantener): ");
                        String nombreNuevo = scanner.nextLine();
                        if (!nombreNuevo.isBlank()) producto.setNombre(nombreNuevo);

                        // Modificar descripcion
                        System.out.print("Nuevo descripcion (Enter para mantener): ");
                        String nuevaDescripcion = scanner.nextLine();
                        if (!nuevaDescripcion.isBlank()) producto.setDescripcion(nuevaDescripcion);

                        // Modificar precio
                        System.out.print("Nuevo precio (Enter para mantener): ");
                        String inputPrecio = scanner.nextLine();
                        if (!inputPrecio.isBlank()) {
                            double nuevoPrecio = Double.parseDouble(inputPrecio);
                            if (nuevoPrecio <= 0) throw new PrecioNoValidoException("El precio debe ser mayor a cero");
                            producto.setPrecio(nuevoPrecio);
                        }

                        // Modificar Stock
                        System.out.print("Nuevo stock (Enter para mantener): ");
                        String inputStock = scanner.nextLine();
                        if (!inputStock.isBlank()) {
                            int nuevoStock = Integer.parseInt(inputStock);
                            if (nuevoStock < 0)
                                throw new StockInsuficienteException("El stock debe cero o un numero mayor");
                            producto.setStock(nuevoStock);
                        }

                        // Guardar Cambios
                        productoRepository.guardar(producto);
                        System.out.println("Producto modificado con exito!");
                    }, () -> System.err.println("Producto no encontrado o ya dado de baja"));

        } catch (NumberFormatException nfe) {
            System.err.println("Error ingrese un numero valido");
        } catch (Exception e) {
            System.err.println("Error al modificar un producto: " + e.getMessage());
        }
    }

    public static void bajaLogicaProducto() {
        listarProductos();
        System.out.print("Ingresa el ID del producto a eliminar: ");
        try {
            Long idABuscar = Long.parseLong(scanner.nextLine());
            productoRepository.buscarPorId(idABuscar)
                    .ifPresentOrElse(producto -> {
                        ProductoDTO productoDTO = ProductoDTO.fromEntidad(producto);
                        System.out.println("-------------------------------");
                        System.out.println("Nombre: " + productoDTO.nombre());
                        System.out.println("Descripcion: " + productoDTO.descripcion());
                        System.out.println("-------------------------------");
                        System.out.println("¿Está seguro que desea eliminar? ");
                        System.out.print("Ingrese 'si' para confirmar o 'no' para cancelar: ");
                        String opcion = scanner.nextLine();
                        Map<String, Runnable> acciones = Map.of(
                                "si", () -> {
                                    productoRepository.eliminarLogico(producto.getId());
                                    System.out.println("Producto " + productoDTO.nombre() +
                                            " eliminado!");
                                },
                                "no", () -> System.out.println("Operacion cancelada")
                        );
                        Optional.ofNullable(acciones.get(opcion.toLowerCase()))
                                .ifPresentOrElse(Runnable::run,
                                        () -> System.out.println("Opcion invalida"));


                    }, () -> System.out.println("Producto no encontrado o ya dado de baja"));
        } catch (NumberFormatException nfe) {
            System.err.println("Error ingrese un numero ");
        } catch (Exception e) {
            System.err.println("Error al eliminar una producto: " + e.getMessage());
        }


    }

    public static void listarProductos() {
        List<Producto> productos = productoRepository.listarActivos();
        System.out.println("Pruductos activos");
        System.out.printf("%-5s %-30s %-20s %-10s %-10s%n",
                "ID", "Nombre", "Categoria", "Stock", "Precio");
        System.out.println("-".repeat(85));
        productos.stream()
                .map(ProductoDTO::fromEntidad)
                .forEach(productoDTO ->
                        System.out.printf("%-5s %-30s %-20s %-10s %-10.2f%n",
                                productoDTO.id(),
                                productoDTO.nombre(),
                                productoDTO.categoriaNombre(),
                                productoDTO.stock(),
                                productoDTO.precio()));
    }


    // Metodos del sub menu usuario
    public static void altaUsuario() {
        try {
            // Ingresar nombre del usuario
            System.out.print("\nIngrese su nombre: ");
            String nombre = scanner.nextLine();
            if (nombre.isBlank()) {
                System.out.print("El nombre no puede estar vacio");
                return;
            }
            System.out.print("Ingrese su apellido: ");
            String apellido = scanner.nextLine();
            if (apellido.isBlank()) {
                System.out.print("El apellido no puede estar vacio");
                return;
            }
            System.out.print("Ingrese su celular(opcional, Enter para omitir): ");
            String celular = scanner.nextLine();
            if (celular.isBlank()) {
                celular = null;
            }

            System.out.print("Ingrese su Email: ");
            String mail = scanner.nextLine();
            // Validar que el mail no esté en uso
            if (usuarioRepository.buscarPorMail(mail).isPresent()) {
                System.err.println("Error: El mail ya está registrado");
                return;
            }
            System.out.print("Ingrese su contraseña: ");
            String contraseña = scanner.nextLine();

            System.out.println("Ingrese su ROL");
            System.out.println("1. ADMIN");
            System.out.println("2. USUARIO");
            System.out.print("Opcion: ");
            int rolOpcion = Integer.parseInt(scanner.nextLine());

            Rol rol;
            switch (rolOpcion) {
                case 1 -> rol = Rol.ADMIN;
                case 2 -> rol = Rol.USUARIO;
                default -> {
                    System.err.println("Error: Opción inválida");
                    return;
                }
            }

            // Crear Usuario y guardar
            Usuario usuarioNuevo = Usuario.builder()
                    .nombre(nombre)
                    .apellido(apellido)
                    .mail(mail)
                    .celular(celular)
                    .contraseña(contraseña)
                    .rol(rol)
                    .build();

            Usuario usuarioGuardar = usuarioRepository.guardar(usuarioNuevo);
            UsuarioDTO usuarioDTO = UsuarioDTO.fromEntidad(usuarioGuardar);
            System.out.println("Usuario guardado con exito!");
            System.out.println("ID: " + usuarioDTO.id());
        } catch (NumberFormatException nfe) {
            System.err.println("Error: ingrese un número válido");
        } catch (Exception e) {
            System.err.println("Error al crear usuario: " + e.getMessage());
        }
    }

    public static void modificarUsuario() {
        listarUsuarios();
        try {
            System.out.print("Ingrese el ID del producto a modificar: ");
            Long idABuscar = Long.parseLong(scanner.nextLine());
            usuarioRepository.buscarPorId(idABuscar)
                    .ifPresentOrElse(usuario -> {
                        UsuarioDTO usuarioDTO = UsuarioDTO.fromEntidad(usuario);
                        // Mostramos datos actuales
                        System.out.println("\n---------- Datos Actuales ----------");
                        System.out.println("Nombre:" + usuarioDTO.nombre());
                        System.out.println("Apellido: " + usuarioDTO.apellido());
                        System.out.println("Celular: " + usuarioDTO.celular());
                        System.out.println("Email:" + usuarioDTO.mail());
                        System.out.println("Contraseña actual: ***");

                        // Modificar nombre
                        System.out.print("Nuevo nombre (Enter para mantener): ");
                        String nombreNuevo = scanner.nextLine();
                        if (!nombreNuevo.isBlank()) usuario.setNombre(nombreNuevo);

                        // Modificar apellido
                        System.out.print("Nuevo Apellido (Enter para mantener): ");
                        String apellidoNuevo = scanner.nextLine();
                        if (!apellidoNuevo.isBlank()) usuario.setApellido(apellidoNuevo);

                        // Modificar celular
                        System.out.print("Nuevo celular (Enter para mantener): ");
                        String celularNuevo = scanner.nextLine();
                        if (!celularNuevo.isBlank()) {
                            if (!celularNuevo.matches("//d+")) {
                                System.err.println("Error: El celular debe contener solo números");
                                return;
                            }
                            usuario.setCelular(celularNuevo);
                        }

                        // Modificar email
                        System.out.print("Nuevo Email (Enter para mantener): ");
                        String nuevoMail = scanner.nextLine();
                        if (!nuevoMail.isBlank()) {
                            usuarioRepository.buscarPorMail(nuevoMail).ifPresent(usuarioExistente -> {
                                if (!usuarioExistente.getId().equals(usuario.getId())) {
                                    System.err.println("Error: El mail ya está registrado");
                                    return;
                                }
                            });
                            usuario.setMail(nuevoMail);
                        }

                        // Modificar contraseña
                        System.out.print("Nueva constraseña (Enter para mantener): ");
                        String nuevaContraseña = scanner.nextLine();
                        if (!nuevaContraseña.isBlank()) usuario.setContraseña(nuevaContraseña);

                        // Guardar Cambio
                        usuarioRepository.guardar(usuario);
                        System.out.println("Usuario modificado con exito!");

                    }, () -> System.err.println("Usuario no encontrado o ya dado de baja"));

        } catch (NumberFormatException nfe) {
            System.err.println("Error ingrese un numero valido");
        } catch (Exception e) {
            System.err.println("Error al modificar un producto: " + e.getMessage());
        }

    }

    public static void bajaLogicaUsuario() {
        listarUsuarios();
        System.out.print("Ingresa el ID del usuario  a eliminar: ");
        try {
            Long idBuscar = Long.parseLong(scanner.nextLine());
            usuarioRepository.buscarPorId(idBuscar)
                    .ifPresentOrElse(usuario -> {
                        UsuarioDTO usuarioDTO = UsuarioDTO.fromEntidad(usuario);
                        System.out.println("-------------------------------");
                        System.out.println("Nombre: " + usuarioDTO.nombre());
                        System.out.println("Apellido: " + usuarioDTO.apellido());
                        System.out.println("-------------------------------");
                        System.out.print("Ingrese 'si' para confirmar o 'no' para cancelar: ");
                        String opcion = scanner.nextLine();
                        Map<String, Runnable> accion = Map.of(
                                "si", () -> {
                                    usuarioRepository.eliminarLogico(usuario.getId());
                                    System.out.println("Nombre: " + usuarioDTO.nombre() +
                                            " Apellido: " + usuarioDTO.apellido() +
                                            " Eliminado con exito");
                                },
                                "no", () -> System.out.println("Operacion cancelada")
                        );
                        Optional.ofNullable(accion.get(opcion.toLowerCase()))
                                .ifPresentOrElse(Runnable::run,
                                        () -> System.out.println("Opcion invalida")
                                );


                    }, () -> System.out.println("usuario no encontrado o ya dado de baja"));
        } catch (NumberFormatException nfe) {
            System.err.println("Error: ingrese un numero " + nfe.getMessage());
        } catch (Exception e) {
            System.err.println("Error al eliminar un usuario: " + e.getMessage());
        }


    }

    public static void listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.listarActivos();
        System.out.println("Usuarios activos");
        System.out.printf("%-5s %-15s %-15s %-30s %-10s%n",
                "ID", "Nombre", "Apellido", "Email", "Rol");
        System.out.println("-".repeat(85));
        usuarios.stream()
                .map(UsuarioDTO::fromEntidad)
                .forEach(usuarioDTO ->
                        System.out.printf("%-5s %-15s %-15s %-30s %-10s%n",
                                usuarioDTO.id(),
                                usuarioDTO.nombre(),
                                usuarioDTO.apellido(),
                                usuarioDTO.mail(),
                                usuarioDTO.rol()
                        )
                );
    }

    public static void buscarUsuarioPorMail() {
        System.out.print("Ingrese el email a buscar: ");
        try {
            String mailABuscar = scanner.nextLine();
            if (mailABuscar.isBlank()) {
                System.err.println("Error: El email no puede estar vacío");
                return;
            }
            if (!mailABuscar.contains("@")) {
                System.err.println("Error: Email inválido (debe contener @)");
                return;
            }
            usuarioRepository.buscarPorMail(mailABuscar)
                    .ifPresentOrElse(usuario -> {
                        UsuarioDTO usuarioDTO = UsuarioDTO.fromEntidad(usuario);
                        System.out.println("\n--- Usuario encontrado ---");
                        System.out.println("ID: " + usuarioDTO.id());
                        System.out.println("Nombre: " + usuarioDTO.nombre());
                        System.out.println("Apellido: " + usuarioDTO.apellido());
                        System.out.println("Mail: " + usuarioDTO.mail());
                        System.out.println("Rol: " + usuarioDTO.rol());
                    }, () -> System.out.println("Usuario no encontrado"));
        } catch (Exception e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }
    }

    // Metodos del sub menu pedido
    public static void altaPedido() {
        List<Usuario> usuarios = usuarioRepository.listarActivos()
                .stream()
                .filter(usuario -> usuario.getRol() == Rol.USUARIO)
                .toList();

        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios activos");
            return;
        }

        // Mostrar usuarios
        System.out.println("\n--- Usuarios disponibles ---");
        System.out.printf("%-5s %-15s %-15s%n",
                "ID", "Nombre", "Apellido");
        System.out.println("-".repeat(85));
        usuarios.stream()
                .map(UsuarioDTO::fromEntidad)
                .forEach(usuarioDTO ->
                        System.out.printf("%-5s %-15s %-15s%n",
                                usuarioDTO.id(),
                                usuarioDTO.nombre(),
                                usuarioDTO.apellido())
                );

        // Seleccionar usuario
        System.out.println("-".repeat(65));
        System.out.print("Ingrese el id del usuario: ");
        try {
            Long usuarioId = Long.parseLong(scanner.nextLine());
            Usuario usuarioSeleccionado = usuarioRepository.buscarPorId(usuarioId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            UsuarioDTO usuarioDTO = UsuarioDTO.fromEntidad(usuarioSeleccionado);
            System.out.println("Usuario seleccionado: " + usuarioDTO.nombre()
                    + " " + usuarioDTO.apellido());

            // Forma de pago
            System.out.println("\nForma de pago:");
            System.out.println("1. TARJETA");
            System.out.println("2. TRANSFERENCIA");
            System.out.println("3. EFECTIVO");
            System.out.print("Opción: ");

            int opcion = Integer.parseInt(scanner.nextLine());
            FormaPago formaDePago = switch (opcion) {
                case 1 -> FormaPago.TARJETA;
                case 2 -> FormaPago.TRANSFERENCIA;
                case 3 -> FormaPago.EFECTIVO;
                default -> throw new IllegalArgumentException("Opcion invalida");
            };
            System.out.println("Forma de pago: " + formaDePago);

            // Listar productos
            List<Map<String, Object>> items = new ArrayList<>();
            boolean agregarMas = true;
            while (agregarMas) {
                listarProductos();
                System.out.print("Ingrese el ID del producto: ");
                Long idProducto = Long.parseLong(scanner.nextLine());

                Optional<Producto> productoOptional = productoRepository.buscarPorId(idProducto);

                if (productoOptional.isEmpty()) {
                    System.err.println("Error: El producto no existe o está dado de baja.");
                    continue;
                }
                Producto producto = productoOptional.get();
                ProductoDTO productoDTO = ProductoDTO.fromEntidad(producto);

                if (!producto.isDisponible()) {
                    System.err.println("Error:Producto no disponible " + productoDTO.nombre());
                    continue;
                }
                System.out.println("Producto seleccionado: " + productoDTO.nombre());
                System.out.print("\nIngrese la cantidad: ");
                int cantidadProducto = Integer.parseInt(scanner.nextLine());

                if (cantidadProducto <= 0) {
                    System.err.println("Error: Cantidad debe ser mayor a 0");
                    continue;
                }

                if (producto.getStock() < cantidadProducto) {
                    System.err.println("Error: Stock insuficiente. Stock disponible: " + producto.getStock());
                    continue;
                }

                Map<String, Object> item = new HashMap<>();
                item.put("productoId", idProducto);
                item.put("cantidad", cantidadProducto);
                items.add(item);
                System.out.println("Producto agregado.");

                boolean respuestaValida = false;
                while (!respuestaValida) {
                    System.out.print("¿Desea agregar otro producto? (si/no): ");
                    String respuesta = scanner.nextLine().trim().toLowerCase();
                    if (respuesta.equals("si")) {
                        respuestaValida = true;
                    } else if (respuesta.equals("no")) {
                        agregarMas = false;
                        respuestaValida = true;
                    } else {
                        System.err.println("Error: Respuesta inválida, ingrese 'si' o 'no'");
                    }
                }
            }

            if (items.isEmpty()) {
                System.out.println("El pedido debe tener al menos un producto");
                return;
            }
            System.out.println("\n--- Resumen de productos ---");
            System.out.printf("%-15s %-15s%n",
                    "Producto ID", "Cantidad");
            System.out.println("-".repeat(40));
            items.forEach(i ->
                    System.out.printf("%-15s %-15s%n",
                            i.get("productoId"),
                            i.get("cantidad"))
            );


            // Transaccion atomica unica
            EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
            EntityManager em = emf.createEntityManager();
            EntityTransaction tx = em.getTransaction();

            try {
                tx.begin();

                Usuario usuarioGestionado = em.find(Usuario.class, usuarioId);

                Pedido pedido = Pedido.builder()
                        .fecha(LocalDate.now())
                        .estado(Estado.PENDIENTE)
                        .formaPago(formaDePago)
                        .build();

                // Agregar pedido al usuario
                usuarioGestionado.addPedido(pedido);

                items.forEach(i -> {
                    Long productosId = (Long) i.get("productoId");
                    int cantidad = (int) i.get("cantidad");

                    Producto productoAgregar = em.find(Producto.class, productosId);
                    pedido.addDetallePedido(cantidad, productoAgregar);
                    productoAgregar.setStock(productoAgregar.getStock() - cantidad);
                });

                pedido.calcularTotal();
                em.persist(pedido);
                tx.commit();

                // Mostrar pedido
                System.out.println("\n--- Pedido creado con éxito ---");
                PedidoDTO pedidoDTO = PedidoDTO.fromEntidad(pedido);
                System.out.println("ID: " + pedidoDTO.id());
                System.out.println("Fecha: " + pedidoDTO.fecha());
                System.out.println("Usuario: " + usuarioDTO.nombre() + " " + usuarioDTO.apellido());

                System.out.println("\n--- Detalle del pedido ---");
                System.out.printf("%-10s %-20s %-10s %-10s%n",
                        "Cantidad", "Productos", "Precio", "Subtotal");
                System.out.println("-".repeat(65));

                // iteramos detalles del pedido
                pedido.getDetalles().stream()
                        .map(DetallePedidoDTO::fromEntidad)
                        .forEach(detallePedidoDTO ->
                                System.out.printf("%-10d %-20s %-10.2f %-10.2f%n",
                                        detallePedidoDTO.cantidad(),
                                        detallePedidoDTO.productoNombre(),
                                        detallePedidoDTO.productoPrecio(),
                                        detallePedidoDTO.subtotal()
                                ));

                System.out.println("-".repeat(65));
                System.out.println("Total: $" + String.format("%.2f", pedidoDTO.total()));
            } catch (RuntimeException re) {
                if (tx.isActive()) tx.rollback();
                System.err.println("Error en la transacción. Se realizó Rollback. Detalle: " + re.getMessage());
            } finally {
                em.close();
            }
        } catch (NumberFormatException nfe) {
            System.err.println("Error: ID debe ser número");
            return;
        } catch (IllegalArgumentException iae) {
            System.err.println("Error: " + iae.getMessage());
            return;
        }


    }

    public static void cambiarEstado() {
        listarPedidos();
        System.out.print("Ingrese el ID del pedido: ");
        try {
            Long idPedido = Long.parseLong(scanner.nextLine());
            pedidoRepository.buscarPorId(idPedido)
                    .ifPresentOrElse(pedido -> {
                        System.out.println("--- Datos actuales ---");
                        PedidoDTO pedidoDTO = PedidoDTO.fromEntidad(pedido);
                        System.out.println("ID: " + pedidoDTO.id());
                        System.out.println("Estado actual: " + pedidoDTO.estado());

                        // Cambiar estado
                        System.out.println("\nEliga la opcion del nuevo estado");
                        System.out.println("1. PENDIENTE\n2. CONFIRMADO\n3. TERMINADO\n4. CANCELADO\n0. Mantener");

                        int opcion = Integer.parseInt(scanner.nextLine());
                        Estado estado = switch (opcion) {
                            case 1 -> Estado.PENDIENTE;
                            case 2 -> Estado.CONFIRMADO;
                            case 3 -> Estado.TERMINADO;
                            case 4 -> Estado.CANCELADO;
                            case 0 -> pedido.getEstado();
                            default -> throw new IllegalArgumentException("Opcion invalida");
                        };

                        // Actualizamos usuario con su nuevo estado
                        pedido.setEstado(estado);

                        // guardamos cambio
                        Pedido pedidoModificado = pedidoRepository.guardar(pedido);

                        System.out.println("Estado cambiado con exito!");
                        PedidoDTO pedidoDTOModificado = PedidoDTO.fromEntidad(pedidoModificado);
                        System.out.println("ID: " + pedidoDTOModificado.id() + ", nuevo estado: " + pedidoDTOModificado.estado().toString());

                    }, () -> System.out.println("usuario no encontrado o ya dado de baja"));

        } catch (NumberFormatException nfe) {
            System.out.println("Error: ingrese un numero: " + nfe.getMessage());
        } catch (Exception e) {
            System.err.println("Error al modificar un producto: " + e.getMessage());
        }


    }

    public static void bajaLogicaPedido() {
        listarPedidos();
        System.out.println("Ingrese el ID del pedido a eliminar");
        try {
            Long idPedido = Long.parseLong(scanner.nextLine());
            pedidoRepository.buscarPorId(idPedido).ifPresentOrElse(
                    pedido -> {
                        PedidoDTO pedidoDTO = PedidoDTO.fromEntidad(pedido);
                        System.out.println("-------------------------------");
                        System.out.println("ID: " + pedidoDTO.id());
                        System.out.println("Total: $" + String.format("%.2f", pedidoDTO.total()));
                        System.out.println("-------------------------------");
                        System.out.print("Ingrese 'si' para confirmar o 'no' para cancelar: ");
                        String opcion = scanner.nextLine();
                        Map<String, Runnable> accion = Map.of(
                                "si", () -> {
                                    pedidoRepository.eliminarLogico(pedido.getId());
                                    System.out.println("ID: " + pedidoDTO.id() +
                                            " Total: $" + String.format("%.2f", pedidoDTO.total()) +
                                            " Eliminado con éxito");
                                },
                                "no", () -> System.out.println("Operacion cancelada")
                        );
                        Optional.ofNullable(accion.get(opcion.toLowerCase()))
                                .ifPresentOrElse(Runnable::run,
                                        () -> System.out.println("Opción invalida")
                                );
                    }, () -> System.out.println("Pedido no encontrado o ya dado de baja")
            );

        } catch (NumberFormatException nfe) {
            System.err.println("Error: ingrese un numero " + nfe.getMessage());
        } catch (Exception e) {
            System.err.println("Error al eliminar un pedido: " + e.getMessage());
        }

    }

    public static void listarPedidos() {
        List<Pedido> pedidos = pedidoRepository.listarActivos();
        System.out.println("Pedidos activos");
        System.out.printf("%-5s %-12s %-15s %-20s %-25s %-12s%n"
                , "ID", "Fecha", "Estado", "Forma de pago", "Nombre(Usuario)", "Total");
        System.out.println("-".repeat(85));
        pedidos.stream()
                .map(PedidoDTO::fromEntidad)
                .forEach(pedidoDTO ->
                        System.out.printf("%-5s %-12s %-15s %-20s %-25s %-12.2f%n",
                                pedidoDTO.id(),
                                pedidoDTO.fecha(),
                                pedidoDTO.estado(),
                                pedidoDTO.formaPago(),
                                pedidoDTO.nombreUsuario(),
                                pedidoDTO.total()
                        )
                );

    }

    public static void pedidoPorUsuario() {
        List<Usuario> usuarios = usuarioRepository.listarActivos()
                .stream()
                .filter(usuario -> usuario.getRol() == Rol.USUARIO)  // ← Filtrar solo USUARIO
                .toList();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios activos");
            return;
        }

        System.out.print("Ingrese el ID del usuario: ");
        try {
            Long idUsuario = Long.parseLong(scanner.nextLine());
            List<Pedido> pedidos = pedidoRepository.buscarPorUsuario(idUsuario);
            if (pedidos.isEmpty()) {
                System.out.println("No hay pedidos para este usuario");
            }
            System.out.println("\n --- Pedidos del usuario ---");
            System.out.printf("%-5s %-12s %-15s %-25s %-15s%n",
                    "ID", "Fecha", "Estado", "Forma de pago", "Total");
            System.out.println("-".repeat(65));
            pedidos.stream()
                    .map(PedidoDTO::fromEntidad)
                    .forEach(
                            pedidoDTO -> System.out.printf(
                                    "%-5s %-12s %-15s %-20s %-12.2f%n",
                                    pedidoDTO.id(),
                                    pedidoDTO.fecha(),
                                    pedidoDTO.estado(),
                                    pedidoDTO.formaPago(),
                                    pedidoDTO.total()

                            )
                    );
        } catch (NumberFormatException nfe) {
            System.err.println("Error: Ingrese un número válido");
        } catch (Exception e) {
            System.err.println("Error al buscar pedidos: " + e.getMessage());
        }
    }

    public static void pedidoPorEstado() {
        System.out.println("\n Seleccione el estado para filtrar pedidos");
        System.out.println("\n1.PENDIENTE \n2.CONFIRMADO \n3.TERMINADO \n4.CANCELADO ");
        System.out.println("-".repeat(40));
        System.out.print("Opción: ");
        try {
            int opcion = Integer.parseInt(scanner.nextLine());
            Estado estado = switch (opcion) {
                case 1 -> Estado.PENDIENTE;
                case 2 -> Estado.CONFIRMADO;
                case 3 -> Estado.TERMINADO;
                case 4 -> Estado.CANCELADO;
                default -> throw new IllegalArgumentException("Error: opcion invalida");
            };
            List<Pedido> pedidos = pedidoRepository.buscarPorEstado(estado);

            if (pedidos.isEmpty()) {
                System.out.println("No hay pedidos con ese estado");
                return;
            }
            System.out.println("\n--- Pedido por estado: " + estado + " ---");
            pedidos.stream()
                    .map(PedidoDTO::fromEntidad)
                    .forEach(pedidoDTO ->
                            System.out.printf("%-5s %-12s %-25s %-20s %-12.2f%n",
                                    pedidoDTO.id(),
                                    pedidoDTO.fecha(),
                                    pedidoDTO.nombreUsuario(),
                                    pedidoDTO.formaPago(),
                                    pedidoDTO.total()));
        } catch (NumberFormatException nfe) {
            System.err.println("Error: Ingrese un número válido");
        } catch (IllegalArgumentException iae) {
            System.err.println("Error: " + iae.getMessage());
        } catch (Exception e) {
            System.err.println("Error al buscar pedidos: " + e.getMessage());
        }


    }

    // Metodos del sub menu reportes
    public static void mostrarProductosPorCategoria() {
        listarCategorias();
        System.out.print("Ingrese el ID de la categoria: ");
        try {
            Long idABuscar = Long.parseLong(scanner.nextLine());
            List<Producto> productos = productoRepository.buscarPorCategoria(idABuscar);
            if (productos.isEmpty()) {
                System.err.println("No hay productos en esa categoria");
                return;
            }
            System.out.printf("%-5s %-20s  %-10s %-10s%n",
                    "ID", "Nombre", "Stock", "Precio");
            System.out.println("-".repeat(65));
            productos.stream()
                    .map(ProductoDTO::fromEntidad)
                    .forEach(dto -> System.out.printf("%-5s %-30s %-10s %-10.2f%n",
                            dto.id(), dto.nombre(), dto.stock(), dto.precio()));
        } catch (NumberFormatException nfe) {
            System.err.println("Error: ingrese un numero valido");
        } catch (Exception e) {
            System.err.println("Error al buscar productos: " + e.getMessage());


        }
    }

    public static void totalFacturado() {
        List<Pedido> pedidos = pedidoRepository.buscarPorEstado(Estado.TERMINADO);

        double totalFacturados = pedidos.stream()
                .map(PedidoDTO::fromEntidad)
                .mapToDouble(PedidoDTO::total)
                .sum();

        System.out.println("\n--- Total Facturado ---");
        System.out.println("Total facturado: " + String.format(Locale.US, "$%.2f", totalFacturados));
    }
}