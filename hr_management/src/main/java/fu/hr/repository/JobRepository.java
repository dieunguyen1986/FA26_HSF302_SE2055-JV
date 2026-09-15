package fu.hr.repository;

import fu.hr.entity.Jobs;

import java.util.List;

public interface JobRepository {
    Jobs save(Jobs job);

    Jobs update(Jobs job);

    void delete(Long id);

    List<Jobs> findAll();

    List<Jobs> findByJobTitle(String jobTitle);
    
}
