package repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;
import model.BugStatus;
import model.Issue;
import model.Priority;
import model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class DatabaseIssueRepository implements IssueRepository {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("bug_tracker_db");

    @Override
    @Transactional
    public void save(Issue issue) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(issue);
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
    public Optional<Issue> findById(UUID id) {
        EntityManager em = emf.createEntityManager();
        return Optional.ofNullable(em.find(Issue.class, id));
    }

    @Override
    public Optional<Issue> findByTitle(String title) {
        EntityManager em = emf.createEntityManager();
        List<Issue> result = em.createQuery(
                        "SELECT i FROM Issue i WHERE i.title = :title",
                        Issue.class)
                .setParameter("title", title)
                .getResultList();

        return result.stream().findFirst();
    }

    @Override
    public List<Issue> findAll() {
        EntityManager em = emf.createEntityManager();
        return em.createQuery(
                "SELECT i FROM Issue i",
                Issue.class
        ).getResultList();
    }

    @Override
    public List<Issue> findByStatus(BugStatus status) {
        EntityManager em = emf.createEntityManager();
        return em.createQuery(
                        "SELECT i FROM Issue i WHERE i.status = :status",
                        Issue.class)
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    public List<Issue> findByPriority(Priority priority) {
        EntityManager em = emf.createEntityManager();
        return em.createQuery(
                        "SELECT i FROM Issue i WHERE i.priority = :priority",
                        Issue.class)
                .setParameter("priority", priority)
                .getResultList();
    }

    @Override
    public List<Issue> findByReporter(User reporter) {
        EntityManager em = emf.createEntityManager();
        return em.createQuery(
                        "SELECT i FROM Issue i WHERE i.reporter = :reporter",
                        Issue.class)
                .setParameter("reporter", reporter)
                .getResultList();
    }

    @Override
    public List<Issue> findCreatedAfter(LocalDateTime date) {
        EntityManager em = emf.createEntityManager();
        return em.createQuery(
                        "SELECT i FROM Issue i WHERE i.createdAt > :date",
                        Issue.class)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public List<Issue> findCreatedBefore(LocalDateTime date) {
        EntityManager em = emf.createEntityManager();
        return em.createQuery(
                        "SELECT i FROM Issue i WHERE i.createdAt < :date",
                        Issue.class)
                .setParameter("date", date)
                .getResultList();
    }

    @Override
    public List<Issue> findByTitleContaining(String titleFragment) {
        EntityManager em = emf.createEntityManager();
        return em.createQuery(
                        "SELECT i FROM Issue i WHERE LOWER(i.title) LIKE LOWER(:fragment)",
                        Issue.class)
                .setParameter("fragment", "%" + titleFragment + "%")
                .getResultList();
    }
}