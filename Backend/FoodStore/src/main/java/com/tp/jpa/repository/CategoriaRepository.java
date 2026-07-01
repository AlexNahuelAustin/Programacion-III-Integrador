package com.tp.jpa.repository;

import com.tp.jpa.model.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class CategoriaRepository extends BaseRepository<Categoria>{
    public CategoriaRepository() {
        super(Categoria.class);
    }


    }

