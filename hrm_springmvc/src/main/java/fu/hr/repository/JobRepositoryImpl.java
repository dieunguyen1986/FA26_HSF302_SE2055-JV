package fu.hr.repository;

import fu.hr.entity.Jobs;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JobRepositoryImpl implements JobRepository {
    private final SessionFactory sessionFactory; // Injection

    @Override
    public Jobs save(Jobs job) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(job);
        return job;
    }

    @Override
    public boolean existsByTitle(String jobTitle) {
        Session session = sessionFactory.getCurrentSession();
        Query<Jobs> query = session.createQuery("FROM Jobs j WHERE j.jobTitle = :jobTitle", Jobs.class);

        query.setParameter("jobTitle", jobTitle);

        return !query.getResultList().isEmpty();

    }

    @Override
    public List<Jobs> findAll() {

        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("FROM Jobs", Jobs.class).getResultList();
    }


}
