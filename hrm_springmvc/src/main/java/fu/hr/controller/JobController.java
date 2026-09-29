package fu.hr.controller;

import fu.hr.entity.Jobs;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

//@Controller
@RestController
public class JobController { // Spring Bean

    @RequestMapping(path = "/jobs", method = {RequestMethod.GET})
    public String getJobs() {

        return "jobs"; // view name - JSON
    }


    @PostMapping("/jobs")
    public String createJob(@RequestParam(name = "jobTitle") String jobTitle,
                            @RequestParam(name = "minSal") String minSalary,
                            @RequestParam(name = "maxSal") String maxSalary
    ) {

        System.out.println("create job title: " + jobTitle);

        // call service

        return "jobs";
    }
}
