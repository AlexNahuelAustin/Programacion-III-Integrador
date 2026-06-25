package com.tp.jpa.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Builder
@Entity
@Table(name = "detalle_pedido")
public class DetallePedido extends Base {
    // Atributos
    @Column(nullable = false)
    private int cantidad;
    @Column(nullable = false)
    private double subtotal;
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;
    @ManyToOne  //
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;


}
