package com.tp.jpa.model.dtos;


import com.tp.jpa.model.Pedido;
import com.tp.jpa.model.enums.Estado;
import com.tp.jpa.model.enums.FormaPago;

import java.time.LocalDate;

public record PedidoDTO(Long id, LocalDate fecha, FormaPago formaPago,
                        Estado estado, double total, String nombreUsuario) {

    public static PedidoDTO fromEntidad(Pedido pedido) {
        return new PedidoDTO(
                pedido.getId(),
                pedido.getFecha(),
                pedido.getFormaPago(),
                pedido.getEstado(),
                pedido.getTotal(),
                pedido.getUsuario().getNombre() + " " + pedido.getUsuario().getApellido()
        );
    }


}