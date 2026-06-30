package com.tp.jpa.repository;


import com.tp.jpa.model.Producto;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ProductoRepository extends BaseRepository<Producto> {
    public ProductoRepository() {
        super(Producto.class);
    }
    public List<Producto> buscarPorCategoria(Long categoriaId) {
        EntityManager em = emf.createEntityManager();
        try {

            // Consulta JPQL: retorna productos activos de una categoría específica
            // Filtra por categoria.id y eliminado=false para excluir bajas lógicas
            String jpql = "SELECT p FROM Producto p WHERE p.categoria.id = :categoriaId AND p.eliminado = false";
            TypedQuery<Producto> query = em.createQuery(jpql,Producto.class);
            query.setParameter("categoriaId",categoriaId);
            return query.getResultList();
        } catch (PersistenceException pe) {
            System.out.println("Error al listar productos por categoria: " + pe.getMessage());
            return List.of();
        }finally {
            em.close();
        }
    }
}
