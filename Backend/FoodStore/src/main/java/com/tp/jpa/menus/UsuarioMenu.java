package com.tp.jpa.menus;
import com.tp.jpa.model.Usuario;
import com.tp.jpa.model.dtos.UsuarioDTO;
import com.tp.jpa.model.enums.Rol;
import com.tp.jpa.repository.UsuarioRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * Menú de gestión de Usuarios.
 * Proporciona operaciones de alta, modificación, baja lógica y listado de usuarios.
 */
public class UsuarioMenu {
    private static UsuarioRepository usuarioRepository;
    private static Scanner scanner;

    public static void setRepositories(UsuarioRepository usuRepo, Scanner scan) {
        //Inyecta las dependencias necesarias para el menú.
        usuarioRepository = usuRepo;
        scanner = scan;
    }

    /**
     * Muestra el submenú de usuarios y procesa las opciones del usuario.
     * Opciones: Alta, Modificar, Baja lógica, Listar, Salir.
     */
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

    // Metodos del sub menu usuario

    /**
     * Crea un nuevo usuario.
     * Valida: nombre, apellido, email (con @), contraseña.
     * Celular es opcional (solo números si se proporciona).
     * Selecciona rol (ADMIN o USUARIO).
     */
    public static void altaUsuario() {
        try {
            // Ingresar nombre del usuario
            System.out.print("\nIngrese su nombre: ");
            String nombre = scanner.nextLine().trim();
            if (nombre.isBlank()) {
                System.out.print("El nombre no puede estar vacio");
                return;
            }

            // Ingresar apellido del usuario
            System.out.print("Ingrese su apellido: ");
            String apellido = scanner.nextLine().trim();
            if (apellido.isBlank()) {
                System.out.print("El apellido no puede estar vacio");
                return;
            }

            // Ingresar celuluar del usuario
            System.out.print("Ingrese su celular(opcional, Enter para omitir): ");
            String celular = scanner.nextLine();

            if (!celular.isBlank()) {
                // Validar que sea solo números
                if (!celular.matches("\\d+")) {
                    System.out.println("Error: El celular debe contener solo números");
                    return;
                }
            } else {
                celular = null;
            }

            // Ingresar email del usuario
            System.out.print("Ingrese su Email: ");
            String mail = scanner.nextLine().trim();

            // Validar que el mail cumpla de ser un email
            if (mail.isBlank() || !mail.contains("@")) {
                System.out.println("Email invalido, debe contener @");
                return;
            }

            // Validar que el mail no esté en uso
            if (usuarioRepository.buscarPorMail(mail).isPresent()) {
                System.err.println("Error: El mail ya está registrado");
                return;
            }

            // Ingresar contraseña del usuario
            System.out.print("Ingrese su contraseña: ");
            String contraseña = scanner.nextLine();

            // Ingresar su ROL
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

    /**
     * Modifica un usuario existente.
     * Permite cambiar: nombre, apellido, celular, email, contraseña.
     * Enter para mantener valores actuales.
     */
    public static void modificarUsuario() {
        listarUsuarios();
        try {
            System.out.print("Ingrese el ID del usuario a modificar: ");
            Long idABuscar = Long.parseLong(scanner.nextLine().trim());
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
                        String nombreNuevo = scanner.nextLine().trim();
                        if (!nombreNuevo.isBlank()) usuario.setNombre(nombreNuevo);

                        // Modificar apellido
                        System.out.print("Nuevo Apellido (Enter para mantener): ");
                        String apellidoNuevo = scanner.nextLine().trim();
                        if (!apellidoNuevo.isBlank()) usuario.setApellido(apellidoNuevo);

                        // Modificar celular
                        System.out.print("Nuevo celular (Enter para mantener): ");
                        String celularNuevo = scanner.nextLine().trim();
                        if (!celularNuevo.isBlank()) {
                            if (!celularNuevo.matches("\\d+")) {
                                System.err.println("Error: El celular debe contener solo números");
                                return;
                            }
                            usuario.setCelular(celularNuevo);
                        }

                        // Modificar email
                        System.out.print("Nuevo Email (Enter para mantener): ");
                        String nuevoMail = scanner.nextLine().trim();
                        if (!nuevoMail.isBlank()) {
                            if (!nuevoMail.contains("@")) {
                                System.err.println("Error: Email invalido, debe contener @");
                                return;
                            }
                            Optional<Usuario> usuarioExitente = usuarioRepository.buscarPorMail(nuevoMail);
                            if (usuarioExitente.isPresent() &&
                                    !usuarioExitente.get().getId().equals(usuario.getId())) {
                                System.out.println("Error: El mail ya está registrado");
                                return;
                            } else {
                                usuario.setMail(nuevoMail);
                            }
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
            System.err.println("Error al modificar un usuario: " + e.getMessage());
        }

    }

    /**
     * Realiza una baja lógica de un usuario.
     * Sus pedidos permanecen sin modificación.
     * Válida que no esté ya eliminado.
     * Solicita confirmación antes de ejecutar.
     */
    public static void bajaLogicaUsuario() {
        listarUsuarios();
        System.out.print("Ingresa el ID del usuario  a eliminar: ");
        try {
            Long idBuscar = Long.parseLong(scanner.nextLine());
            usuarioRepository.buscarPorId(idBuscar)
                    .ifPresentOrElse(usuario -> {
                        if (usuario.isEliminado()) {
                            System.out.println("Usuario no existente o ya eliminado");
                            return;
                        }
                        UsuarioDTO usuarioDTO = UsuarioDTO.fromEntidad(usuario);
                        System.out.println("-------------------------------");
                        System.out.println("Nombre: " + usuarioDTO.nombre());
                        System.out.println("Apellido: " + usuarioDTO.apellido());
                        System.out.println("-------------------------------");
                        System.out.print("Ingrese 'si' para confirmar o 'no' para cancelar: ");
                        String opcion = scanner.nextLine().trim().toLowerCase();
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
                    }, () -> System.out.println("Usuario no encontrado o ya eliminado"));
        } catch (NumberFormatException nfe) {
            System.err.println("Error: ingrese un numero " + nfe.getMessage());
        } catch (Exception e) {
            System.err.println("Error al eliminar un usuario: " + e.getMessage());
        }
    }

    /**
     * Lista todos los usuarios activos (no eliminados).
     * Muestra: ID, Nombre, Apellido, Email, Rol en formato tabla.
     */
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

    /**
     * Busca un usuario por email.
     * Valida que el email contenga @ y no esté vacío.
     * Muestra los datos del usuario si lo encuentra.
     */
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

}
