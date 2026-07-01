package com.tp.jpa;
import com.tp.jpa.init.DataLoader;
import com.tp.jpa.menus.*;
import com.tp.jpa.model.dtos.*;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.PedidoRepository;
import com.tp.jpa.repository.ProductoRepository;
import com.tp.jpa.repository.UsuarioRepository;
import com.tp.jpa.util.JPAUtil;
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
                    case 1 -> {
                        CategoriaMenu.setRepositories(categoriaRepository, productoRepository, scanner);
                        CategoriaMenu.subMenuCategoria();
                    }
                    case 2 -> {
                        ProductoMenu.setRepositories(productoRepository, categoriaRepository, scanner);
                        ProductoMenu.subMenuProducto();
                    }
                    case 3 -> {
                        UsuarioMenu.setRepositories(usuarioRepository, scanner);
                        UsuarioMenu.subMenuUsuario();
                    }
                    case 4 -> {
                        PedidoMenu.setRepositories(pedidoRepository, usuarioRepository, productoRepository, scanner);
                        PedidoMenu.subMenuPedido();
                    }
                    case 5 -> {
                        ReportesMenu.setRepositories(categoriaRepository, productoRepository, pedidoRepository, usuarioRepository, scanner);
                        ReportesMenu.subMenuReportes();

                    }
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


}