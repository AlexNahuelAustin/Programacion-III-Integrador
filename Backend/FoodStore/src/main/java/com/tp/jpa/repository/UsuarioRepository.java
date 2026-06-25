package com.tp.jpa.repository;

import com.tp.jpa.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class UsuarioRepository extends BaseRepository<Usuario>{
    public UsuarioRepository() {
        super(Usuario.class);
    }

    // Buscar usuario por su mail
    public Optional<Usuario> buscarPorMail(String mail){
        EntityManager em = emf.createEntityManager();
        try {
            // JPQL: busca usuario activo por email
            // Retorna Optional para manejar mail no registrado
            String jpql = "SELECT u FROM Usuario u WHERE u.mail = :mail AND u.eliminado = false";
            TypedQuery<Usuario> query = em.createQuery(jpql,Usuario.class);
            query.setParameter("mail",mail);
            List<Usuario> resultado = query.getResultList();
            return resultado.isEmpty() ? Optional.empty():Optional.of(resultado.getFirst());
        }catch (PersistenceException pe){
            System.err.println("Error al buscar usuario por mail: " + pe.getMessage());
            return Optional.empty();
        }finally {
            em.close();
        }
    }
}
