package fu.hr.controller;

import fu.hr.dto.EmployeeRequest;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @PostMapping
    public String createEmployee(@ModelAttribute EmployeeRequest request) {

        System.out.println("Emp: " + request);
        return "success";
    }
}
