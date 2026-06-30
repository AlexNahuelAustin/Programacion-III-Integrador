package com.tp.jpa.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
@Table(name = "productos")
public class Producto extends Base {
    // Atributos
    @EqualsAndHashCode.Include
    @Column(nullable = false,length = 50)
    private String nombre;
    @Column(nullable = false)
    private double precio;
    @Column(nullable = false,length = 250)
    private String descripcion;
    @Column(nullable = false)
    private int stock;
    private String imagen;
    @Column(nullable = false)
    private boolean disponible;
    @ManyToOne
    @JoinColumn(name = "categoria_id",nullable = false)
    private Categoria categoria;

    // === SETTERS PERSONALIZADOS CON VALIDACIONES ===
    public void setPrecio(double precio) {
        if (precio <= 0) {
            throw new IllegalArgumentException("Precio debe ser mayor a 0");
        }
        this.precio = precio;
    }

    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("El stock debe ser positivo");
        }
        this.stock = stock;
    }

}