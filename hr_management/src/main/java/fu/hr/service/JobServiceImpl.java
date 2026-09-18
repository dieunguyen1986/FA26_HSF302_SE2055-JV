package fu.hr.service;

import fu.hr.entity.Jobs;
import fu.hr.exception.JobInvalidException;
import fu.hr.repository.JobRepository;
import fu.hr.repository.JobRepositoryImpl;

import java.util.List;

public class JobServiceImpl implements JobService {
    private JobRepository jobRepository = new JobRepositoryImpl();

    @Override
    public Jobs save(Jobs job) {
        // Validate

        if (job.getMaxSalary() < job.getMinSalary()) {
            throw new JobInvalidException("Max sal must greater than min sal");
        }

        if (jobRepository.existsByTitle(job.getJobTitle())) {
            throw new RuntimeException("Job title is existing, can not be save.");
        }

        // Rules

        return jobRepository.save(job);

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
}
