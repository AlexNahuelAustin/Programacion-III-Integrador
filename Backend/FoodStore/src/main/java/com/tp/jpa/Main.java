package com.tp.jpa;

import com.tp.jpa.exceptions.PrecioNoValidoException;
import com.tp.jpa.exceptions.StockInsuficienteException;
import com.tp.jpa.init.DataLoader;
import com.tp.jpa.model.Categoria;
import com.tp.jpa.model.Producto;
import com.tp.jpa.model.dtos.CategoriaDTO;
import com.tp.jpa.model.dtos.ProductoDTO;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.ProductoRepository;
import com.tp.jpa.util.JPAUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    static CategoriaRepository categoriaRepository = new CategoriaRepository();
    static ProductoRepository productoRepository = new ProductoRepository();
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        DataLoader.cargarDatos(categoriaRepository, productoRepository);
        menuPrincipal();
        JPAUtil.close();
    }
    private static void menuPrincipal() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n+================================+");
            System.out.println("|   Gestion de pedidos - JPA    |");
            System.out.println("+================================+");
            System.out.println("\n==========================");
            System.out.println("        Menu Principal");
            System.out.println("==========================\n");
            System.out.println("  1. Gestion de Categorias");
            System.out.println("  2. Gestion de Productos");
            System.out.println("  3. Reportes");
            System.out.println("  0. Salir");
            System.out.println("----------------------------");
            System.out.print("Seleccione una opcion: ");
            try {
                int opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1 -> subMenuCategoria();
                    case 2 -> subMenuProducto();
                    case 3 -> subMenuReportes();
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

    // Submenu de reportes
    public static void subMenuReportes() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- Submenu: Reportes ---");
            System.out.println("1. Productos por categoria (JPQL)");
            System.out.println("0. Volver al menu principal");
            try {
                System.out.print("Opcion: ");
                int opcion = Integer.parseInt(scanner.nextLine());
                if (opcion == 1) {
                    mostrarProductosPorCategoria();
                } else if (opcion == 0) {
                    salir = true;
                } else {
                    System.err.println("Opcion invalida");
                }
            } catch (NumberFormatException nfe) {
                System.err.println("Error ingrese un numero valido" + nfe.getMessage());
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
                System.out.print("El nombre no puede estar vacio");
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
            System.out.print("\nIngresa el ID a modificar: ");
            Long idABuscar = Long.parseLong(scanner.nextLine());
            categoriaRepository.buscarPorId(idABuscar)
                    .ifPresentOrElse(categoria -> {
                        CategoriaDTO categoriaDTO = CategoriaDTO.fromEntidad(categoria);
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
        categorias.forEach(categoria -> {
            CategoriaDTO categoriaDTO = CategoriaDTO.fromEntidad(categoria);
            System.out.printf("%-10s %-20s %-30s%n",
                    categoriaDTO.id(), categoriaDTO.nombre(), categoriaDTO.descripcion());
        });

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
                    .categoria(categoria)
                    .build();

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
                                    System.out.println("Producto " + productoDTO.nombre() + " eliminado!");
                                },
                                "no", () -> System.out.println("Operacion cancelada")
                        );
                        Optional.ofNullable(acciones.get(opcion.toLowerCase()))
                                .ifPresentOrElse(Runnable::run,
                                        () -> System.out.println("Opcion invalida"));


                    }, () -> System.out.println("Producto no encontrado o ya dado de baja"));
        } catch (NumberFormatException nfe) {
            System.err.println("Error ingrese un  valido");
        } catch (Exception e) {
            System.err.println("Error al modificar una producto: " + e.getMessage());
        }


    }

    public static void listarProductos() {
        List<Producto> productos = productoRepository.listarActivos();
        System.out.println("Pruductos activos");
        System.out.printf("%-5s %-30s %-20s %-10s %-10s%n",
                "ID", "Nombre", "Categoria", "Stock", "Precio");
        System.out.println("-".repeat(85));
        productos.forEach(producto -> {
            ProductoDTO productoDTO = ProductoDTO.fromEntidad(producto);
            System.out.printf("%-5s %-30s %-20s %-10s %-10.2f%n",
                    productoDTO.id(),
                    productoDTO.nombre(),
                    productoDTO.categoriaNombre(),
                    productoDTO.stock(),
                    productoDTO.precio());
        });
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
                    "ID", "Nombre", "Stock", "Precio" );
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
}