package repository;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Class holding all users
 *
 * @author Artur Twardzik
 * @version 0.6
 */
public class DatabaseUserRepository implements UserRepository {
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("bug_tracker_db");

    @Override
    public void save(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // persist for new entity, merge for detached
            if (user.getId() == null) {
                em.persist(user);
            } else {
                em.merge(user);
            }

            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.username = :username",
                    User.class
            );
            query.setParameter("username", username);

            List<User> result = query.getResultList();
            return result.stream().findFirst();
        }
    }

    @Override
    public Optional<User> findById(UUID id) {
        try (EntityManager em = emf.createEntityManager()) {
            User user = em.find(User.class, id);
            return Optional.ofNullable(user);
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        try (EntityManager em = emf.createEntityManager()) {
            Long count = em.createQuery(
                            "SELECT COUNT(u) FROM User u WHERE u.username = :username",
                            Long.class
                    ).setParameter("username", username)
                    .getSingleResult();

            return count > 0;
        }
    }

    @Override
    public List<User> findAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                    "SELECT u FROM User u ORDER BY u.createdOn",
                    User.class
            ).getResultList();
        }
    }
}
