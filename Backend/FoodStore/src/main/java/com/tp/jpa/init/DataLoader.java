package com.tp.jpa.init;

import com.tp.jpa.model.Categoria;
import com.tp.jpa.model.Pedido;
import com.tp.jpa.model.Producto;
import com.tp.jpa.model.Usuario;
import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import com.tp.jpa.model.enums.Rol;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.PedidoRepository;
import com.tp.jpa.repository.ProductoRepository;
import com.tp.jpa.repository.UsuarioRepository;

import java.time.LocalDate;

public class DataLoader {
    public static void cargarDatos(CategoriaRepository categoriaRepository, ProductoRepository productoRepository, UsuarioRepository usuarioRepository, PedidoRepository pedidoRepository) {
        if (!categoriaRepository.listarActivos().isEmpty()) return;

        // --- CATEGORIAS ---
        Categoria categoria1 = categoriaRepository.guardar(Categoria.builder()
                .nombre("Pizzas")
                .descripcion("Pizzas artesanales con masa fresca")
                .build());

        Categoria categoria2 = categoriaRepository.guardar(Categoria.builder()
                .nombre("Hamburguesas")
                .descripcion("Hamburguesas gourmet con ingredientes frescos")
                .build());

        Categoria categoria3 = categoriaRepository.guardar(Categoria.builder()
                .nombre("Bebidas")
                .descripcion("Gaseosas, jugos y bebidas frias")
                .build());

        Categoria categoria4 = categoriaRepository.guardar(Categoria.builder()
                .nombre("Postres")
                .descripcion("Postres caseros y helados")
                .build());


        // --- PRODUCTOS ---
        // Pizzas
        Producto producto1 = Producto.builder()
                .nombre("Pizza Muzzarella")
                .precio(9000)
                .descripcion("Pizza clásica con salsa de tomate y muzzarella derretida")
                .stock(32)
                .imagen("pizza-muzzarella.png")
                .disponible(true)
                .build();
        categoria1.agregarProductos(producto1);
        productoRepository.guardar(producto1);

        Producto producto2 = Producto.builder()
                .nombre("Pizza Napolitana")
                .precio(5200)
                .descripcion("Pizza con rodajas de tomate fresco, ajo y albahaca")
                .stock(15)
                .imagen("pizza-napolitana.png")
                .disponible(true)
                .build();
        categoria1.agregarProductos(producto2);
        productoRepository.guardar(producto2);

        Producto producto3 = Producto.builder()
                .nombre("Pizza Especial 4 Quesos")
                .precio(6800)
                .descripcion("Muzzarella, provolone, roquefort y parmesano")
                .stock(10)
                .imagen("pizza-cuatro-quesos.png")
                .disponible(true)
                .build();
        categoria1.agregarProductos(producto3);
        productoRepository.guardar(producto3);

        Producto producto4 = Producto.builder()
                .nombre("Pizza Fugazzeta")
                .precio(5500)
                .descripcion("Pizza rellena de muzzarella con cebolla caramelizada")
                .stock(10)
                .imagen("pizza-fuggazeta.png")
                .disponible(true)
                .build();
        categoria1.agregarProductos(producto4);
        productoRepository.guardar(producto4);

// Hamburguesas
        Producto producto5 = Producto.builder()
                .nombre("Hamburguesa Clásica")
                .precio(3800)
                .descripcion("Medallón de carne, lechuga, tomate, cebolla y mayo")
                .stock(30)
                .imagen("hamburguesa-clasica.png")
                .disponible(true)
                .build();
        categoria2.agregarProductos(producto5);
        productoRepository.guardar(producto5);

        Producto producto6 = Producto.builder()
                .nombre("Hamburguesa BBQ Bacon")
                .precio(5100)
                .descripcion("Doble medallón, bacon crocante y salsa barbacoa ahumada")
                .stock(25)
                .imagen("hamburguesa-bbq-bacon.png")
                .disponible(true)
                .build();
        categoria2.agregarProductos(producto6);
        productoRepository.guardar(producto6);

        Producto producto7 = Producto.builder()
                .nombre("Hamburguesa Veggie")
                .precio(4200)
                .descripcion("Medallón de lentejas y garbanzo, cheddar vegano y rúcula")
                .stock(0)
                .imagen("hamburguesas-veggies.png")
                .disponible(false)
                .build();
        categoria2.agregarProductos(producto7);
        productoRepository.guardar(producto7);

// Bebidas
        Producto producto8 = Producto.builder()
                .nombre("Coca-Cola 500ml")
                .precio(1200)
                .descripcion("Gaseosa Coca-Cola fría, botella personal")
                .stock(100)
                .imagen("bebida-cola.jpg")
                .disponible(true)
                .build();
        categoria3.agregarProductos(producto8);
        productoRepository.guardar(producto8);

        Producto producto9 = Producto.builder()
                .nombre("Jugo de Naranja Natural")
                .precio(1800)
                .descripcion("Jugo exprimido en el momento, vaso 400ml")
                .stock(40)
                .imagen("jugo-naranja.png")
                .disponible(true)
                .build();
        categoria3.agregarProductos(producto9);
        productoRepository.guardar(producto9);

        Producto producto10 = Producto.builder()
                .nombre("Agua Mineral 500ml")
                .precio(800)
                .descripcion("Agua mineral sin gas, botella personal")
                .stock(150)
                .imagen("agua-500ml.png")
                .disponible(true)
                .build();
        categoria3.agregarProductos(producto10);
        productoRepository.guardar(producto10);

        // --- USUARIO ---

        Usuario usuario1 = Usuario.builder()
                .nombre("Admin")
                .apellido("Sistema")
                .mail("admin@foodstore.com")
                .celular("1123456789")
                .contraseña("admin123")
                .rol(Rol.ADMIN)
                .build();
        usuarioRepository.guardar(usuario1);


        Usuario usuario2 = Usuario.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .mail("juan@mail.com")
                .celular("1198765432")
                .contraseña("juan123")
                .rol(Rol.USUARIO)
                .build();
        usuarioRepository.guardar(usuario2);

        Usuario usuario3 = Usuario.builder()
                .nombre("María")
                .apellido("García")
                .mail("maria@mail.com")
                .celular("1187654321")
                .contraseña("maria123")
                .rol(Rol.USUARIO)
                .build();
        usuarioRepository.guardar(usuario3);

        Usuario usuario4 = Usuario.builder()
                .nombre("Carlos")
                .apellido("López")
                .mail("carlos@mail.com")
                .celular("1176543210")
                .contraseña("carlos123")
                .rol(Rol.USUARIO)
                .build();
        usuarioRepository.guardar(usuario4);


        Pedido pedido1 = Pedido.builder()
                .fecha(LocalDate.now().minusDays(5))
                .estado(Estado.TERMINADO)
                .formaPago(FormaPago.TARJETA)
                .build();

        pedido1.addDetallePedido(2, producto1);
        pedido1.addDetallePedido(1, producto5);
        pedido1.calcularTotal();

        usuario2.addPedido(pedido1);
        pedidoRepository.guardar(pedido1);

        Pedido pedido2 = Pedido.builder()
                .fecha(LocalDate.now().minusDays(3))
                .estado(Estado.TERMINADO)
                .formaPago(FormaPago.EFECTIVO)
                .build();

        pedido2.addDetallePedido(1, producto2);
        pedido2.addDetallePedido(2, producto8);
        pedido2.calcularTotal();

        usuario3.addPedido(pedido2);
        pedidoRepository.guardar(pedido2);

        Pedido pedido3 = Pedido.builder()
                .fecha(LocalDate.now().minusDays(1))
                .estado(Estado.CONFIRMADO)
                .formaPago(FormaPago.TRANSFERENCIA)
                .build();

        pedido3.addDetallePedido(1, producto3);
        pedido3.addDetallePedido(1, producto6);
        pedido3.calcularTotal();

        usuario2.addPedido(pedido3);
        pedidoRepository.guardar(pedido3);

        Pedido pedido4 = Pedido.builder()
                .fecha(LocalDate.now())
                .estado(Estado.PENDIENTE)
                .formaPago(FormaPago.TARJETA)
                .build();

        pedido4.addDetallePedido(3, producto9);
        pedido4.addDetallePedido(1, producto4);
        pedido4.calcularTotal();

        usuario4.addPedido(pedido4);
        pedidoRepository.guardar(pedido4);



    }
}
