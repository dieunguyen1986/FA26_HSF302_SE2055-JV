package fu.hr.repository;

import fu.hr.entity.Jobs;

public interface JobRepository {
    Jobs save(Jobs job);

    boolean existsByTitle(String jobTitle);
}
