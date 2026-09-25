package fu.hr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
//@RestController
public class JobController { // Spring Bean

    @RequestMapping(path = "/jobs", method = {RequestMethod.GET})
    public String getJobs(){

        return "jobs"; // view name - JSON
    }
}
