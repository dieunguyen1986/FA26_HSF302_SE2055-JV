package fu.hr.repository;

import fu.hr.entity.Jobs;
import fu.hr.utils.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class JobRepositoryImpl implements JobRepository {
    @Override
    public Jobs save(Jobs job) {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;

        try {
            entityManager = JpaUtil.getEntityManager();

            transaction = entityManager.getTransaction();
            transaction.begin();
            // Insert into
            entityManager.persist(job);
            transaction.commit();

        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();

            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return job;
    }

    @Override
    public Jobs update(Jobs job) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = JpaUtil.getEntityManager();) {
            transaction = entityManager.getTransaction();
            transaction.begin();
            // UPDATE
            entityManager.merge(job);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }

        return job;
    }

    @Override
    public void delete(Long id) {
        EntityTransaction transaction = null;
        try (EntityManager entityManager = JpaUtil.getEntityManager();) {

            Jobs job = entityManager.find(Jobs.class, id);

            entityManager.remove(job);

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

    }

    @Override
    public List<Jobs> findAll() {
        try (EntityManager entityManager = JpaUtil.getEntityManager();) {
            return entityManager.createQuery("FROM Jobs", Jobs.class).getResultList(); // JPQl -> Jakarta Persistence Query Language

        }
    }

    @Override
    public List<Jobs> findByJobTitle(String jobTitle) {
        try (EntityManager entityManager = JpaUtil.getEntityManager();) {
            TypedQuery query = entityManager.createQuery("SELECT j FROM Jobs j " +
                    "WHERE j.jobTitle LIKE %:jobTitle%", Jobs.class); // table name: jobs

            query.setParameter("jobTitle", jobTitle);

            return query.getResultList();
        }
    }

    @Override
    public boolean existsByTitle(String title) {
        try (EntityManager entityManager = JpaUtil.getEntityManager();) {
            TypedQuery query = entityManager
                    .createQuery("FROM Jobs j WHERE j.jobTitle LIKE :jobTitle", Jobs.class)
                    .setParameter("jobTitle", "%" + title + "%");

            return !query.getResultList().isEmpty();
        }

    }
}
