package fu.hr.controller;

import fu.hr.entity.Jobs;
import fu.hr.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

//@Controller
@RestController
@RequiredArgsConstructor
public class JobController { // Spring Bean
    private final JobService jobService;

    @RequestMapping(path = "/jobs", method = {RequestMethod.GET})
    public String getJobs() {

        return "jobs"; // view name - JSON
    }


    @PostMapping("/jobs")
    public Jobs createJob(@RequestParam(name = "jobTitle") String jobTitle,
                          @RequestParam(name = "minSal") Double minSalary,
                          @RequestParam(name = "maxSal") Double maxSalary
    ) {

        System.out.println("create job title: " + jobTitle);

        // call service
        Jobs job = Jobs.builder().jobTitle(jobTitle)
                .minSalary(minSalary)
                .maxSalary(maxSalary)
                .build();

        return jobService.save(job);
    }
}
