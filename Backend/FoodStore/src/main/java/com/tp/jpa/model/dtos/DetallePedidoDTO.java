package com.tp.jpa.model.dtos;


import com.tp.jpa.model.DetallePedido;

public record DetallePedidoDTO(int cantidad,
                               double subtotal,
                               String productoNombre,
                               double productoPrecio) {
    public static DetallePedidoDTO fromEntidad(DetallePedido detallePedido) {
        return new DetallePedidoDTO(
                detallePedido.getCantidad(),
                detallePedido.getSubtotal(),
                detallePedido.getProducto().getNombre(),
                detallePedido.getProducto().getPrecio()
        );
    }
}
