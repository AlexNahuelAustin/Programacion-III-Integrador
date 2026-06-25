package com.tp.jpa.model;


import com.tp.jpa.exceptions.CantidadInvalidaException;
import com.tp.jpa.exceptions.ProductoNoDisponibleException;
import com.tp.jpa.exceptions.ProductoNoValidoException;
import com.tp.jpa.exceptions.StockInsuficienteException;
import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@SuperBuilder
@ToString(exclude = {"detalles"})
@Entity
@Table(name = "pedidos")
public class Pedido extends Base implements Calculable {
    // Atributos
    @Builder.Default
    private LocalDate fecha = LocalDate.now();
    @Enumerated(EnumType.STRING)
    private Estado estado;
    private double total;
    @Enumerated(EnumType.STRING)
    private FormaPago formaPago;
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    @Builder.Default
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "pedido")
    private Set<DetallePedido> detalles = new HashSet<>();


    // Metodos propios
    public void addDetallePedido(int cantidad, Producto producto) {
        // Validar que producto no sea null
        if (producto == null) {
            throw new ProductoNoValidoException("El pruducto no puede ser null");
        }
        // validar que Cantidad sea positiva
        if (cantidad <= 0) {
            throw new CantidadInvalidaException("La cantidad debe mayor a cero");
        }
        // Validar que el producto este disponible
        if (!producto.isDisponible()) {
            throw new ProductoNoDisponibleException("El producto no esta disponible" + producto.getNombre());
        }


        // Validar si hay Stock
        if (producto.getStock() < cantidad) {
            throw new StockInsuficienteException("El producto no esta disponible" + producto.getNombre());
        }

        // Descontamos stock
        producto.setStock(producto.getStock() - cantidad);

        // Detalle
        DetallePedido detalle;
        detalle = DetallePedido.builder()
                .cantidad(cantidad)
                .subtotal(producto.getPrecio() * cantidad)
                .producto(producto)
                .pedido(this)
                .build();
        this.detalles.add(detalle);
        this.calcularTotal();
    }

    public Optional<DetallePedido> findDetallePedidoByProducto(Producto producto) {
        return detalles.stream()
                .filter(detalle -> detalle.getProducto().equals(producto))
                .findFirst();
    }


    public void deleteDetallePedidoByProducto(Producto producto) {
        detalles.removeIf(detalle -> detalle.getProducto().equals(producto));
        calcularTotal();
    }

    @Override
    public void calcularTotal() {
        this.total = detalles.stream()
                .mapToDouble(DetallePedido::getSubtotal)
                .sum();
    }

    public int contarItems() {
        return this.detalles.stream()
                .mapToInt(DetallePedido::getCantidad)
                .sum();
    }
}

