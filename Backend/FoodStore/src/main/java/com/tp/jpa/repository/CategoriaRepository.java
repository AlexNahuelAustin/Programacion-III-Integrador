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

    public Optional<Categoria> buscarPorNombre(String nombre){
        EntityManager em = emf.createEntityManager();
        try {
            // Consulta jpql para consultar a la base de datos si existe el nombre de esa categoria
            // Y que no este eliminado logicamente.
            String jpql = "SELECT c FROM Categoria c WHERE c.nombre = :nombre AND c.eliminado = false";
            TypedQuery<Categoria> query = em.createQuery(jpql, Categoria.class);
            query.setParameter("nombre",nombre);
            List<Categoria> resultado = query.getResultList();
            return resultado.isEmpty() ?Optional.empty() :Optional.of(resultado.getFirst());
        }catch (PersistenceException pe) {
            System.err.println("Error al buscar categoría por nombre: " + pe.getMessage());
            return Optional.empty();
        } finally { em.close(); }
    }
    }

