package com.tp.jpa.repository;

import com.tp.jpa.model.Base;
import com.tp.jpa.util.JPAUtil;
import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;

public abstract class BaseRepository<T extends Base> {

    protected final EntityManagerFactory emf;
    private final Class<T> entityClass;

    protected BaseRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.emf = JPAUtil.getEntityManagerFactory();
    }

    protected Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * Alta o actualización. Si la entidad no tiene id usa persist() (alta: la
     * propia instancia recibe el ID generado); si ya tiene id usa merge()
     * (actualización). Retorna la entidad gestionada, de la cual debe leerse
     * el ID generado. Hace rollback ante excepción.
     */
    public T guardar(T entity) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T resultado;
            if (entity.getId() == null) {
                em.persist(entity);
                resultado = entity;
            } else {
                resultado = em.merge(entity);
            }
            tx.commit();
            return resultado;
        } catch (PersistenceException pe) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error de persistencia al guardar: " + pe.getMessage());
            throw pe;
        }catch (RuntimeException e){
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error inesperado: " + e.getMessage());
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Busca por ID. Retorna Optional.of(entidad) si existe, Optional.empty()
     * en caso contrario.
     */
    public Optional<T> buscarPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            T entity = em.find(entityClass, id);
            return Optional.ofNullable(entity);
        } finally {
            em.close();
        }
    }

    /**
     * Lista las entidades activas (eliminado = false). Construye la JPQL con
     * el nombre simple de la clase para que funcione con todas las entidades.
     */
    public List<T> listarActivos() {
        EntityManager em = emf.createEntityManager();
        try {
            // JPQL: SELECT e FROM NombreEntidad e WHERE e.eliminado = false
            String jpql = "SELECT e FROM " + getEntityClass().getSimpleName()
                    + " e WHERE e.eliminado = false";
            return em.createQuery(jpql, entityClass).getResultList();
        }catch (PersistenceException pe) {
            System.err.println("Error al listar activos: " + pe.getMessage());
            return List.of();
        } finally {
            em.close();
        }
    }

    /**
     * Baja lógica: busca la entidad por ID, marca eliminado = true y
     * sincroniza con merge() (la entidad ya tiene id). Retorna true si la
     * encontró y la dio de baja; false si no existe.
     */
    public boolean eliminarLogico(Long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            T entity = em.find(entityClass, id);
            if (entity == null) {
                return false;
            }
            tx.begin();
            entity.setEliminado(true);
            em.merge(entity);
            tx.commit();
            return true;
        } catch (PersistenceException pe) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error de persistencia al eliminar: " + pe.getMessage());
            throw pe;
        }catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error inesperado al eliminar: " + e.getMessage());
            throw e;
        } finally {
            em.close();
        }
    }
}
