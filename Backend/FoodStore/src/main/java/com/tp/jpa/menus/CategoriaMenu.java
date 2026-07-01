package com.tp.jpa.menus;
import com.tp.jpa.model.Categoria;
import com.tp.jpa.model.dtos.CategoriaDTO;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.ProductoRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/*
 * Menú de gestión de Categorías.
 * Proporciona operaciones de alta, modificación, baja lógica y listado de categorías.
 */
public class CategoriaMenu {
    private static CategoriaRepository categoriaRepository;
    private static ProductoRepository productoRepository;
    private static Scanner scanner;


    public static void setRepositories(CategoriaRepository catRepo, ProductoRepository prodRepo, Scanner scan) {
        //Inyecta las dependencias necesarias para el menú.
        categoriaRepository = catRepo;
        productoRepository = prodRepo;
        scanner = scan;
    }

    /**
     * Muestra el submenú de categorías y procesa las opciones del usuario.
     * Opciones: Alta, Modificar, Baja lógica, Listar, Salir.
     */
    public static void subMenuCategoria() {
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

    // Métodos del sub menu de categoria
    /**
     * Crea una nueva categoría.
     * Válida que el nombre no esté vacío y no sea duplicado.
     * Muestra el ID de la categoría creada.
     */
    public static void altaCategoria() {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();
            if (nombre.isBlank()) {
                System.out.print("Error: El nombre no puede estar vacio");
                return;
            }

            boolean yaExiste = categoriaRepository.listarActivos().stream()
                    .anyMatch(cat -> cat.getNombre().equalsIgnoreCase(nombre));

            if (yaExiste) {
                System.out.println("Error: Ya existe una categoría con ese nombre.");
                return;
            }

            System.out.print("Descripcion: ");
            String descripcion = scanner.nextLine().trim();
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

    /**
     * Modifica una categoría existente.
     * Permite cambiar nombre y descripción de forma opcional (Enter para mantener).
     * Válida que el nuevo nombre no sea duplicado.
     */
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
                        String nombreNuevo = scanner.nextLine().trim();
                        if (!nombreNuevo.isBlank()) {
                            categoria.setNombre(nombreNuevo);
                        }

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

    /**
     * Realiza una baja lógica de una categoría.
     * Elimina lógicamente todos los productos asociados a la categoría.
     * Solicita confirmación antes de ejecutar la baja.
     */
    public static void bajaLogicaCategoria() {
        listarCategorias();
        System.out.print("Ingresa el ID de la categoria a eliminar: ");
        try {
            Long idBuscar = Long.parseLong(scanner.nextLine().trim());
            categoriaRepository.buscarPorId(idBuscar)
                    .ifPresentOrElse(categoria -> {
                        if (categoria.isEliminado()) {
                            System.out.println("Categoria no existente o ya eliminada");
                            return;
                        }
                        CategoriaDTO categoriaDTO = CategoriaDTO.fromEntidad(categoria);
                        System.out.println("-------------------------------");
                        System.out.println("Nombre: " + categoriaDTO.nombre());
                        System.out.println("-------------------------------");
                        System.out.print("Ingrese 'si' para confirmar o 'no' para cancelar: ");
                        String opcion = scanner.nextLine().trim().toLowerCase();

                        Map<String, Runnable> accion = Map.of(
                                "si", () -> {
                                    productoRepository.buscarPorCategoria(categoria.getId())
                                            .forEach(p -> productoRepository.eliminarLogico(p.getId()));
                                    categoriaRepository.eliminarLogico(categoria.getId());
                                    System.out.println("Categoria " + categoriaDTO.nombre() + " eliminada con exito");
                                },
                                "no", () -> System.out.println("Operacion cancelada")
                        );

                        Optional.ofNullable(accion.get(opcion))
                                .ifPresentOrElse(Runnable::run,
                                        () -> System.out.println("Opcion invalida")
                                );
                    }, () -> System.out.println("Categoria no encontrada o ya eliminada"));

        } catch (NumberFormatException nfe) {
            System.err.println("Error: ingrese un número válido");
        } catch (Exception e) {
            System.err.println("Error al modificar una categoria: " + e.getMessage());
        }
    }

    /**
     * Lista todas las categorías activas (no eliminadas).
     * Muestra: ID, Nombre, Descripción en formato tabla.
     */
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

}
