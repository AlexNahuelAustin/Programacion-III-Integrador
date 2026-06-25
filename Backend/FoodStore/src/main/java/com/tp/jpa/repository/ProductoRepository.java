package com.tp.jpa.repository;


import com.tp.jpa.model.Producto;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ProductoRepository extends BaseRepository<Producto> {
    public ProductoRepository() {
        super(Producto.class);
    }
    public List<Producto> buscarPorCategoria(Long categoriaId) {

        try (EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager()) {
            String jpql = "SELECT p FROM Producto p WHERE p.categoria.id = :categoriaId AND p.eliminado = false";
            TypedQuery<Producto> query = em.createQuery(jpql, Producto.class);
            query.setParameter("categoriaId", categoriaId);
            return query.getResultList();

        } catch (Exception e) {
            System.err.println("Error al listar productos por categoria" + e.getMessage());
            return List.of();
        }
    }
}
