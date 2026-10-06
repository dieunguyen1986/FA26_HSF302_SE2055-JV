package fu.hr.repository;

import fu.hr.entity.Jobs;

import java.util.List;

public interface JobRepository {
    Jobs save(Jobs job);

    boolean existsByTitle(String jobTitle);

    List<Jobs> findAll();
}
