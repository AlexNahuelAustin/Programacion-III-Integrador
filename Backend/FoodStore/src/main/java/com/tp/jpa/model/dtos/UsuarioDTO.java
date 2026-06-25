package com.tp.jpa.model.dtos;


import com.tp.jpa.model.Usuario;

public record UsuarioDTO(String nombre, String apellido, String mail, String celular) {

    public static UsuarioDTO fromEntidad(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getMail(),
                usuario.getCelular()
        );
    }
}