package com.tp.jpa.repository;


import com.tp.jpa.model.Pedido;
import com.tp.jpa.model.enums.Estado;
import jakarta.persistence.EntityManager;

import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class PedidoRepository extends BaseRepository<Pedido> {

    public PedidoRepository() {
        super(Pedido.class);
    }

    // Buscar pedidos por id del ususario
    public List<Pedido> buscarPorUsuario(Long idUsuario){
        EntityManager em = emf.createEntityManager();
        try {
            // Consulta JPQL: retorna todos los pedidos activos de un usuario dado su ID
            // Filtra por eliminado = false para excluir pedidos dados de baja lógica
            String jpql = "SELECT p FROM Pedido p WHERE p.usuario.id = :uId AND p.eliminado = false";
            TypedQuery<Pedido> query = em.createQuery(jpql,Pedido.class);
            query.setParameter("uId",idUsuario);
            return query.getResultList();
        } catch (PersistenceException pe) {
            System.err.println("Error al listar pedidos por usuario: " + pe.getMessage());
            return List.of();
        }finally {
            em.close();
        }
    }


    // Buscar los pedidos por estado
    public List<Pedido> buscarPorEstado(Estado estado){
        EntityManager em = emf.createEntityManager();
    try {
        // Consulta JPQL: retorna todos los pedidos activos con un estado específico
        // Útil para filtrar PENDIENTE, CONFIRMADO, TERMINADO o CANCELADO
        String jpql ="SELECT p FROM Pedido p WHERE p.estado =:estado AND p.eliminado = false";
        TypedQuery<Pedido> query = em.createQuery(jpql,Pedido.class);
        query.setParameter("estado",estado);
         return query.getResultList();
    } catch (PersistenceException pe) {
        System.err.println("Error al buscar pedidos por estado: " + pe.getMessage());
        return List.of();
    }finally {
        em.close();
    }
    }
}
