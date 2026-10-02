package fu.hr.service;

import fu.hr.dto.JobResponse;
import fu.hr.entity.Jobs;
import fu.hr.exception.JobInvalidException;
import fu.hr.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("jobService")
public class JobServiceImpl implements JobService {
//    @Autowired // Inject by Field - reflection
    private JobRepository jobRepository; // Inject

    // Inject by constructor
    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

//    @Autowired
//    public void setJobRepository(JobRepository jobRepository) {
//        this.jobRepository = jobRepository;
//    }

    @Override
    @Transactional
    public JobResponse save(Jobs job) {

        // Validate

        if (job.getMaxSalary() < job.getMinSalary()) {
            throw new JobInvalidException("Max sal must greater than min sal");
        }

        if (jobRepository.existsByTitle(job.getJobTitle())) {
            throw new RuntimeException("Job title is existing, can not be save.");
        }

        // Rules
        return toJobResponse(jobRepository.save(job));

    }

    @Override
    public Jobs update(Jobs job) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Jobs> findAll() {

        return null;
    }

    @Override
    public List<Jobs> findByJobTitle(String jobTitle) {
        return null;
    }

    //
    private JobResponse toJobResponse(Jobs job) {

        JobResponse jobResponse = new JobResponse();
        jobResponse.setId(job.getId());
        jobResponse.setJobTitle(job.getJobTitle());
        jobResponse.setMaxSalary(job.getMaxSalary());
        jobResponse.setMinSalary(job.getMinSalary());

        return  jobResponse;

    }
}
