package com.tp.jpa.init;

import com.tp.jpa.model.Categoria;
import com.tp.jpa.model.Producto;
import com.tp.jpa.repository.CategoriaRepository;
import com.tp.jpa.repository.ProductoRepository;

public class DataLoader {
    public static void cargarDatos(CategoriaRepository categoriaRepository
            , ProductoRepository productoRepository) {
        if (!categoriaRepository.listarActivos().isEmpty()) return;
        // Categorias
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


        // Creamos los 10 pruductos
        // Pizzas
        Producto producto1 = Producto.builder()
                .nombre("Pizza Muzzarella")
                .precio(9000)
                .descripcion("Pizza clásica con salsa de tomate y muzzarella derretida")
                .stock(3)
                .imagen("pizza-muzzarella.png")
                .disponible(true)
                .categoria(categoria1)
                .build();
        productoRepository.guardar(producto1);

        Producto producto2 = Producto.builder()
                .nombre("Pizza Napolitana")
                .precio(5200)
                .descripcion("Pizza con rodajas de tomate fresco, ajo y albahaca")
                .stock(15)
                .imagen("pizza-napolitana.png")
                .disponible(true)
                .categoria(categoria1)
                .build();
        productoRepository.guardar(producto2);

        Producto producto3 = Producto.builder()
                .nombre("Pizza Especial 4 Quesos")
                .precio(6800)
                .descripcion("Muzzarella, provolone, roquefort y parmesano")
                .stock(10)
                .imagen("pizza-cuatro-quesos.png")
                .disponible(true)
                .categoria(categoria1)
                .build();
        productoRepository.guardar(producto3);

        Producto producto4 = Producto.builder()
                .nombre("Pizza Fugazzeta")
                .precio(5500)
                .descripcion("Pizza rellena de muzzarella con cebolla caramelizada")
                .stock(10)
                .imagen("pizza-fuggazeta.png")
                .disponible(true)
                .categoria(categoria1)
                .build();
        productoRepository.guardar(producto4);

        // Hamburguesas
        Producto producto5 = Producto.builder()
                .nombre("Hamburguesa Clásica")
                .precio(3800)
                .descripcion("Medallón de carne, lechuga, tomate, cebolla y mayo")
                .stock(30)
                .imagen("hamburguesa-clasica.png")
                .disponible(true)
                .categoria(categoria2)
                .build();
        productoRepository.guardar(producto5);

        Producto producto6 = Producto.builder()
                .nombre("Hamburguesa BBQ Bacon")
                .precio(5100)
                .descripcion("Doble medallón, bacon crocante y salsa barbacoa ahumada")
                .stock(25)
                .imagen("hamburguesa-bbq-bacon.png")
                .disponible(true)
                .categoria(categoria2)
                .build();
        productoRepository.guardar(producto6);

        Producto producto7 = Producto.builder()
                .nombre("Hamburguesa Veggie")
                .precio(4200)
                .descripcion("Medallón de lentejas y garbanzo, cheddar vegano y rúcula")
                .stock(0)
                .imagen("hamburguesas-veggies.png")
                .disponible(false)
                .categoria(categoria2)
                .build();
        productoRepository.guardar(producto7);

        // Bebidas
        Producto producto8 = Producto.builder()
                .nombre("Coca-Cola 500ml")
                .precio(1200)
                .descripcion("Gaseosa Coca-Cola fría, botella personal")
                .stock(100)
                .imagen("bebida-cola.jpg")
                .disponible(true)
                .categoria(categoria3)
                .build();
        productoRepository.guardar(producto8);

        Producto producto9 = Producto.builder()
                .nombre("Jugo de Naranja Natural")
                .precio(1800)
                .descripcion("Jugo exprimido en el momento, vaso 400ml")
                .stock(40)
                .imagen("jugo-naranja.png")
                .disponible(true)
                .categoria(categoria3)
                .build();
        productoRepository.guardar(producto9);

        Producto producto10 = Producto.builder()
                .nombre("Agua Mineral 500ml")
                .precio(800)
                .descripcion("Agua mineral sin gas, botella personal")
                .stock(150)
                .imagen("agua-500ml.png")
                .disponible(true)
                .categoria(categoria3)
                .build();
        productoRepository.guardar(producto10);


    }
}
