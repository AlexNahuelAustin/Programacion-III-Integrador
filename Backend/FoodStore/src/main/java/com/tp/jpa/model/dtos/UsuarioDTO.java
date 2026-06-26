package com.tp.jpa.model.dtos;


import com.tp.jpa.model.Usuario;

public record UsuarioDTO(Long id, String nombre, String apellido, String mail, String celular,String rol) {

    public static UsuarioDTO fromEntidad(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getMail(),
                usuario.getCelular(),
                usuario.getRol().name()
        );
    }
}