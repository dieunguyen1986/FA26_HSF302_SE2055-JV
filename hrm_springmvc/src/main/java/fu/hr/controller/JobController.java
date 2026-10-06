package fu.hr.controller;

import fu.hr.dto.JobResponse;
import fu.hr.entity.Jobs;
import fu.hr.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
//@RestController
@RequiredArgsConstructor
public class JobController { // Spring Bean
    private final JobService jobService;

    @RequestMapping(path = "/jobs", method = {RequestMethod.GET})
    public String getJobs(Model model) {

        List<JobResponse> jobs = jobService.findAll();
        model.addAttribute("jobs", jobs);

        return "job-management"; // view name - JSON
    }


    @PostMapping("/jobs")
    public ModelAndView createJob(@RequestParam(name = "jobTitle") String jobTitle,
                                  @RequestParam(name = "minSal") Double minSalary,
                                  @RequestParam(name = "maxSal") Double maxSalary,
                                  Model model

    ) {

        System.out.println("create job title: " + jobTitle);

        // call service
        Jobs job = Jobs.builder().jobTitle(jobTitle)
                .minSalary(minSalary)
                .maxSalary(maxSalary)
                .build();

        jobService.save(job);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("job-management");

        modelAndView.addObject("message", "Create successful!");
//        return

        return modelAndView;
    }
}
