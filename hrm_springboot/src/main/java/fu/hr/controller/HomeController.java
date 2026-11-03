package fu.hr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employees")
public class HomeController {
    @GetMapping
    public String profileView() {
        return "views/employee/employee-profile";
    }
}
