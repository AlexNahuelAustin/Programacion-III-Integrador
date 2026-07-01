package com.tp.jpa.menus;
import com.tp.jpa.model.Categoria;
import com.tp.jpa.model.Pedido;
import com.tp.jpa.model.Producto;
import com.tp.jpa.model.Usuario;
import com.tp.jpa.model.dtos.CategoriaDTO;
import com.tp.jpa.model.dtos.PedidoDTO;
import com.tp.jpa.model.dtos.ProductoDTO;
import com.tp.jpa.model.dtos.UsuarioDTO;
import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.Rol;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.PedidoRepository;
import com.tp.jpa.repository.ProductoRepository;
import com.tp.jpa.repository.UsuarioRepository;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Menú de Reportes.
 * Proporciona análisis y reportes de categorías, productos y pedidos.
 */
public class ReportesMenu {
    private static CategoriaRepository categoriaRepository;
    private static ProductoRepository productoRepository;
    private static PedidoRepository pedidoRepository;
    private static UsuarioRepository usuarioRepository;
    private static Scanner scanner;

    public static void setRepositories(CategoriaRepository catRepo, ProductoRepository prodRepo,
                                       PedidoRepository pedRepo,UsuarioRepository usuRepo, Scanner scan) {
        //Inyecta las dependencias necesarias para el menú.
        categoriaRepository = catRepo;
        productoRepository = prodRepo;
        pedidoRepository = pedRepo;
        usuarioRepository = usuRepo;
        scanner = scan;
    }

    /**
     * Muestra el submenú de reportes y procesa las opciones del usuario.
     * Opciones: Productos por categoría, Pedidos por usuario, Pedidos por estado, Total facturado, Salir.
     */
    public static void subMenuReportes() {
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

    /**
     * Muestra un reporte de productos agrupados por categoría.
     * Permite seleccionar una categoría y listar sus productos.
     */
    public static void mostrarProductosPorCategoria() {
        List<Categoria> categorias = categoriaRepository.listarActivos();

        if (categorias.isEmpty()) {
            System.out.println("No hay categorías activas");
            return;
        }

        System.out.println("\n--- Categorías disponibles ---");
        System.out.printf("%-5s %-20s%n", "ID", "Nombre");
        System.out.println("-".repeat(40));
        categorias.stream()
                .map(CategoriaDTO::fromEntidad)  // ← DTO
                .forEach(categoriaDTO ->
                        System.out.printf("%-5s %-20s%n",
                                categoriaDTO.id(),
                                categoriaDTO.nombre())
                );

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

    /**
     * Muestra un reporte de pedidos de un usuario específico.
     * Filtra solo usuarios de tipo USUARIO (no admin).
     */
    public static void pedidoPorUsuario() {
        List<Usuario> usuarios = usuarioRepository.listarActivos()
                .stream()
                .filter(usuario -> usuario.getRol() == Rol.USUARIO)  // ← Filtrar solo USUARIO
                .toList();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios activos");
            return;
        }
        System.out.println("\n--- Usuarios disponibles ---");
        System.out.printf("%-5s %-15s %-15s%n", "ID", "Nombre", "Apellido");
        System.out.println("-".repeat(85));
        usuarios.stream()
                .map(UsuarioDTO::fromEntidad)
                .forEach(usuarioDTO ->
                        System.out.printf("%-5s %-15s %-15s%n",
                                usuarioDTO.id(),
                                usuarioDTO.nombre(),
                                usuarioDTO.apellido())
                );
        System.out.println("-".repeat(35));
        System.out.print("Ingrese el ID del usuario: ");
        try {
            Long idUsuario = Long.parseLong(scanner.nextLine());
            List<Pedido> pedidos = pedidoRepository.buscarPorUsuario(idUsuario);
            if (pedidos.isEmpty()) {
                System.out.println("No hay pedidos para este usuario");
            }

            System.out.println("\n--- Usuarios disponibles ---");
            System.out.printf("%-5s %-15s %-15s%n", "ID", "Nombre", "Apellido");
            System.out.println("-".repeat(85));
            usuarios.stream()
                    .map(UsuarioDTO::fromEntidad)
                    .forEach(usuarioDTO ->
                            System.out.printf("%-5s %-15s %-15s%n",
                                    usuarioDTO.id(),
                                    usuarioDTO.nombre(),
                                    usuarioDTO.apellido())
                    );

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

    /**
     * Muestra un reporte de pedidos filtrados por estado.
     * Estados disponibles: PENDIENTE, CONFIRMADO, TERMINADO, CANCELADO.
     */
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
            System.out.printf("%-5s %-15s %-25s %-20s %-12s%n",
                    "ID", "Fecha", "Usuario", "Forma de pago", "Total");
            System.out.println("-".repeat(80));
            pedidos.stream()
                    .map(PedidoDTO::fromEntidad)
                    .forEach(pedidoDTO ->
                            System.out.printf("%-5s %-15s %-25s %-20s %-12.2f%n",
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


    /**
     * Calcula y muestra el total facturado.
     * Suma de todos los pedidos con estado TERMINADO.
     */
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
