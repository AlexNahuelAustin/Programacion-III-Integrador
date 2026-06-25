package com.tp.jpa.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(exclude = "productos")
@EqualsAndHashCode(callSuper = true, exclude = {"descripcion", "productos"})
@Entity
@Table(name = "categorias")
public class Categoria extends Base {
    // Atributos
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String descripcion;
    @Builder.Default
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Producto> productos = new HashSet<>();

    // Agregar Productos
    public void agregarProductos(Producto producto) {
        productos.add(producto);
        producto.setCategoria(this);
    }
}

