package com.tp.jpa.menus;
import com.tp.jpa.exceptions.PrecioNoValidoException;
import com.tp.jpa.exceptions.StockInsuficienteException;
import com.tp.jpa.model.Categoria;
import com.tp.jpa.model.Producto;
import com.tp.jpa.model.dtos.CategoriaDTO;
import com.tp.jpa.model.dtos.ProductoDTO;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.ProductoRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Menú de gestión de Productos.
 * Proporciona operaciones de alta, modificación, baja lógica y listado de productos.
 */
public class ProductoMenu {
    private static ProductoRepository productoRepository;
    private static CategoriaRepository categoriaRepository;
    private static Scanner scanner;


    public static void setRepositories(ProductoRepository prodRepo, CategoriaRepository catRepo, Scanner scan) {
        //Inyecta las dependencias necesarias para el menú.
        productoRepository = prodRepo;
        categoriaRepository = catRepo;
        scanner = scan;
    }

    /**
     * Muestra el submenú de productos y procesa las opciones del usuario.
     * Opciones: Alta, Modificar, Baja lógica, Listar, Salir.
     */
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

    // Metodos del sub menu de producto

    /**
     * Crea un nuevo producto.
     * Valida nombre, precio (> 0), stock (>= 0), selecciona categoría y disponibilidad.
     */
    public static void altaProducto() {
        try {
            System.out.print("\nnombre: ");
            String nombre = scanner.nextLine().trim();
            if (nombre.isBlank()) {
                System.out.println("El nombre no puede estar vacio");
                return;
            }

            boolean yaExiste = productoRepository.listarActivos().stream()
                    .anyMatch(p -> p.getNombre().equalsIgnoreCase(nombre));
            if (yaExiste) {
                System.err.println("Error: Ya existe un producto con ese nombre.");
                return;
            }

            System.out.print("\nDescripcion: ");
            String descripcion = scanner.nextLine().trim();

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
            Long categoriaId = Long.parseLong(scanner.nextLine().trim());

            Categoria categoria = categoriaRepository.buscarPorId(categoriaId)
                    .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));

            Producto productoNuevo = Producto.builder()
                    .nombre(nombre)
                    .descripcion(descripcion)
                    .precio(precio)
                    .stock(stock)
                    .categoria(categoria)
                    .imagen("Imagen.png")
                    .disponible(true)
                    .build();


            Producto guardado = productoRepository.guardar(productoNuevo);
            ProductoDTO productoDTO = ProductoDTO.fromEntidad(guardado);
            System.out.println("Producto Creado con exito!");
            System.out.println("ID: " + productoDTO.id());


        } catch (IllegalArgumentException iae) {
            System.err.println("Error de validacion: " + iae.getMessage());
        } catch (Exception e) {
            System.err.println("Error al crear producto" + e.getMessage());
        }
    }

    /**
     * Modifica un producto existente.
     * Permite cambiar: nombre(Verificando que no se repita el nombre), descripción, precio, stock, categoría.
     * Enter para mantener valores actuales.
     */
    public static void modificarProducto() {
        listarProductos();
        try {
            System.out.print("Ingrese el ID del producto a modificar: ");
            Long idABuscar = Long.parseLong(scanner.nextLine().trim());
            productoRepository.buscarPorId(idABuscar)
                    .ifPresentOrElse(producto -> {
                        if (producto.isEliminado()){
                            System.err.println("Producto no encontrado o ya dado de baja");
                            return;
                        }
                        ProductoDTO productoDTO = ProductoDTO.fromEntidad(producto);
                        System.out.println("\n---------- Datos Actuales ----------");
                        System.out.println("Nombre actual: " + productoDTO.nombre());
                        System.out.println("Descripcion actual: " + productoDTO.descripcion());
                        System.out.println("precio actual: " + productoDTO.precio());
                        System.out.println("Stock actual: " + productoDTO.stock());

                        // Modificar nombre
                        System.out.print("Nuevo nombre (Enter para mantener): ");
                        String nombreNuevo = scanner.nextLine().trim();
                        if (!nombreNuevo.isBlank()) producto.setNombre(nombreNuevo);
                        boolean yaExiste = productoRepository.listarActivos().stream()
                                .anyMatch(p -> p.getNombre().equalsIgnoreCase(nombreNuevo));
                        if (yaExiste) {
                            System.err.println("Error: Ya existe otro producto activo con ese nombre. No se modificó el nombre.");
                        } else {
                            producto.setNombre(nombreNuevo);
                        }

                        // Modificar descripcion
                        System.out.print("Nuevo descripcion (Enter para mantener): ");
                        String nuevaDescripcion = scanner.nextLine().trim();
                        if (!nuevaDescripcion.isBlank()) producto.setDescripcion(nuevaDescripcion);

                        // Modificar precio
                        System.out.print("Nuevo precio (Enter para mantener): ");
                        String inputPrecio = scanner.nextLine().trim();
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

        } catch (
                NumberFormatException nfe) {
            System.err.println("Error ingrese un numero valido");
        } catch (
                Exception e) {
            System.err.println("Error al modificar un producto: " + e.getMessage());
        }
    }

    /**
     * Realiza una baja lógica de un producto.
     * Valida que no esté ya eliminado.
     * Solicita confirmación antes de ejecutar.
     */
    public static void bajaLogicaProducto() {
        listarProductos();
        System.out.print("Ingresa el ID del producto a eliminar: ");
        try {
            Long idBuscar = Long.parseLong(scanner.nextLine().trim());
            productoRepository.buscarPorId(idBuscar)
                    .ifPresentOrElse(producto -> {
                        if (producto.isEliminado()) {
                            System.out.println("Producto no encontrado o ya eliminado");
                            return;
                        }

                        ProductoDTO productoDTO = ProductoDTO.fromEntidad(producto);
                        System.out.println("-------------------------------");
                        System.out.println("Nombre: " + productoDTO.nombre());
                        System.out.println("Descripcion: " + productoDTO.descripcion());
                        System.out.println("-------------------------------");
                        System.out.print("Ingrese 'si' para confirmar o 'no' para cancelar: ");
                        String opcion = scanner.nextLine().trim().toLowerCase();

                        Map<String, Runnable> accion = Map.of(
                                "si", () -> {
                                    productoRepository.eliminarLogico(producto.getId());
                                    System.out.println("Producto " + productoDTO.nombre() + " eliminado con exito");
                                },
                                "no", () -> System.out.println("Operacion cancelada")
                        );
                        Optional.ofNullable(accion.get(opcion))
                                .ifPresentOrElse(Runnable::run,
                                        () -> System.out.println("Opcion invalida")
                                );
                    }, () -> System.out.println("Producto no encontrado o ya eliminado"));

        } catch (NumberFormatException nfe) {
            System.err.println("Error: ingrese un número válido");
        } catch (Exception e) {
            System.err.println("Error al dar de baja el producto: " + e.getMessage());
        }
    }

    /**
     * Lista todos los productos activos (no eliminados).
     * Muestra: ID, Nombre, Categoría, Stock, Precio en formato tabla.
     */
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


    /*
    * Lista privada para listar un producto
    * */
    private static void listarCategorias() {
        List<Categoria> categorias = categoriaRepository.listarActivos();
        System.out.println("--- Categorias activas ---");
        System.out.printf("%-10s %-20s %20s%n", "ID", "Nombre", "Descripcion");
        System.out.println("-".repeat(85));
        categorias.stream().map(CategoriaDTO::fromEntidad)
                .forEach(categoriaDTO ->
                        System.out.printf("%-10s %-20s %-30s%n",
                                categoriaDTO.id(), categoriaDTO.nombre(), categoriaDTO.descripcion()));

    }
}
