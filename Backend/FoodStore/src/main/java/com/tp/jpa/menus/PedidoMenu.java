package com.tp.jpa.menus;

import com.tp.jpa.model.Pedido;
import com.tp.jpa.model.Producto;
import com.tp.jpa.model.Usuario;
import com.tp.jpa.model.dtos.DetallePedidoDTO;
import com.tp.jpa.model.dtos.PedidoDTO;
import com.tp.jpa.model.dtos.ProductoDTO;
import com.tp.jpa.model.dtos.UsuarioDTO;
import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import com.tp.jpa.model.enums.Rol;
import com.tp.jpa.repository.PedidoRepository;
import com.tp.jpa.repository.ProductoRepository;
import com.tp.jpa.repository.UsuarioRepository;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.*;

/**
 * Menú de gestión de Pedidos.
 * Proporciona operaciones de alta, cambio de estado, baja lógica, búsqueda y reportes.
 */
public class PedidoMenu {
    private static PedidoRepository pedidoRepository;
    private static UsuarioRepository usuarioRepository;
    private static ProductoRepository productoRepository;
    private static Scanner scanner;

    public static void setRepositories(PedidoRepository pedRepo, UsuarioRepository usuRepo,
                                       ProductoRepository prodRepo, Scanner scan) {
        //Inyecta las dependencias necesarias para el menú.
        pedidoRepository = pedRepo;
        usuarioRepository = usuRepo;
        productoRepository = prodRepo;
        scanner = scan;
    }

    /**
     * Muestra el submenú de pedidos y procesa las opciones del usuario.
     * Opciones: Alta, Cambiar estado, Baja lógica, Listar, Buscar por usuario, Buscar por estado, Salir.
     */
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

    /**
     * Crea un nuevo pedido de forma transaccional.
     * Selecciona usuario USUARIO (no admin, no eliminado), forma de pago, y agrega productos.
     * Valida disponibilidad, stock y cantidad de productos.
     * Realiza la operación en una transacción atómica única.
     */
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
            if (usuarioSeleccionado.isEliminado()) {
                throw new IllegalArgumentException("El usuario ha sido dado de baja");
            }
            if (usuarioSeleccionado.getRol() != Rol.USUARIO) {
                throw new IllegalArgumentException("El usuario debe ser de tipo USUARIO");
            }
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

                // VERIFICAR SI EL PRODUCTO YA EXISTE EN ITEMS
                Map<String, Object> productoExistente = items.stream()
                        .filter(item -> item.get("productoId").equals(idProducto))
                        .findFirst()
                        .orElse(null);

                if (productoExistente != null) {
                    // Si existe, aumentar cantidad
                    int cantidadActual = (int) productoExistente.get("cantidad");
                    productoExistente.put("cantidad", cantidadActual + cantidadProducto);
                } else {
                    // Si no existe, agregarlo nuevo
                    Map<String, Object> item = new HashMap<>();
                    item.put("productoId", idProducto);
                    item.put("cantidad", cantidadProducto);
                    items.add(item);
                }

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
                System.out.printf("%-10s %-25s %-10s %-10s%n",
                        "Cantidad", "Productos", "Precio", "Subtotal");
                System.out.println("-".repeat(65));

                // iteramos detalles del pedido
                pedido.getDetalles().stream()
                        .map(DetallePedidoDTO::fromEntidad)
                        .forEach(detallePedidoDTO ->
                                System.out.printf("%-10d %-25s %-10.2f %-10.2f%n",
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
        } catch (IllegalArgumentException iae) {
            System.err.println("Error: " + iae.getMessage());
        }
    }

    /**
     * Cambia el estado de un pedido existente.
     * Estados disponibles: PENDIENTE, CONFIRMADO, TERMINADO, CANCELADO.
     */
    public static void cambiarEstado() {
        listarPedidos();
        System.out.print("Ingrese el ID del pedido: ");
        try {
            Long idPedido = Long.parseLong(scanner.nextLine());
            pedidoRepository.buscarPorId(idPedido)
                    .ifPresentOrElse(pedido -> {
                        if (pedido.isEliminado()) {
                            System.out.println("Pedido no encontrado o ya dado de baja");
                            return;
                        }
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

                    }, () -> System.out.println("Pedido no encontrado o ya dado de baja"));

        } catch (NumberFormatException nfe) {
            System.out.println("Error: ingrese un numero valido: " + nfe.getMessage());
        } catch (Exception e) {
            System.err.println("Error al modificar el estado del pedido: " + e.getMessage());
        }


    }

    /**
     * Realiza una baja lógica de un pedido.
     * Valida que no esté ya eliminado.
     * Solicita confirmación antes de ejecutar.
     */
    public static void bajaLogicaPedido() {
        listarPedidos();
        System.out.println("Ingrese el ID del pedido a eliminar");
        try {
            Long idPedido = Long.parseLong(scanner.nextLine());
            pedidoRepository.buscarPorId(idPedido).ifPresentOrElse(
                    pedido -> {
                        if (pedido.isEliminado()) {
                            System.out.println("Pedido no encontrado o ya dado de baja");
                            return;
                        }
                        PedidoDTO pedidoDTO = PedidoDTO.fromEntidad(pedido);
                        System.out.println("-------------------------------");
                        System.out.println("ID: " + pedidoDTO.id());
                        System.out.println("Total: $" + String.format("%.2f", pedidoDTO.total()));
                        System.out.println("-------------------------------");
                        System.out.print("Ingrese 'si' para confirmar o 'no' para cancelar: ");
                        String opcion = scanner.nextLine().trim().toLowerCase();
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

    /**
     * Lista todos los pedidos activos (no eliminados).
     * Muestra: ID, Fecha, Estado, Forma de pago, Usuario, Total en formato tabla.
     */
    public static void listarPedidos() {
        List<Pedido> pedidos = pedidoRepository.listarActivos();

        System.out.println("Pedidos activos");
        System.out.printf("%-5s %-12s %-15s %-20s %-25s %-12s%n"
                , "ID", "Fecha", "Estado", "Forma de pago", "Nombre(Usuario)", "Total");
        System.out.println("-".repeat(90));
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

    /**
     * Busca pedidos por usuario.
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
            System.out.println("\n--- Pedidos del usuario ---");
            System.out.printf("%-5s %-12s %-15s %-20s %-12s%n",
                    "ID", "Fecha", "Estado", "Forma de pago", "Total");
            System.out.println("-".repeat(70));
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
     * Busca pedidos por estado.
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
     * Lista todos los productos activos (no eliminados).
     * Auxiliar para seleccionar productos al crear un pedido.
     */
    private static void listarProductos() {
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
                                productoDTO.precio())
                );
    }
}
