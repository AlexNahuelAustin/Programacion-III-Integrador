package com.tp.jpa.model.dtos;


import com.tp.jpa.model.Producto;

public record ProductoDTO(Long id, String nombre, double precio, String descripcion,
                          boolean disponible,
                          String categoriaNombre,int stock) {
    public static ProductoDTO fromEntidad(Producto producto) {
        return new ProductoDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getDescripcion(),
                producto.isDisponible(),
                producto.getCategoria().getNombre(),
                producto.getStock()
        );
    }
}
