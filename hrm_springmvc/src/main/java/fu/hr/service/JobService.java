package fu.hr.service;

import fu.hr.dto.JobResponse;
import fu.hr.entity.Jobs;

import java.util.List;

public interface JobService {
    JobResponse save(Jobs job);

    Jobs update(Jobs job);

    void delete(Long id);

    List<Jobs> findAll();

    List<Jobs> findByJobTitle(String jobTitle);
}
