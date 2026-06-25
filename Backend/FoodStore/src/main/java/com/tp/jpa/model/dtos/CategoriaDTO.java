package com.tp.jpa.model.dtos;


import com.tp.jpa.model.Categoria;


public record CategoriaDTO(
        Long id, String nombre,
        String descripcion

) {
    public static CategoriaDTO fromEntidad(Categoria categoria) {
        return new CategoriaDTO(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getDescripcion()
        );
    }


}
