package fu.hr.controller;

import fu.hr.dto.DepartmentRequest;
import fu.hr.dto.DepartmentResponse;
import fu.hr.service.DepartmentService;
import fu.hr.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/departments")
@RequiredArgsConstructor
@Slf4j
public class DepartmentController {
    private final DepartmentService departmentService;
    private final EmployeeService employeeService;

    @PostMapping
    public String create(@ModelAttribute("department") @Valid DepartmentRequest departmentRequest,
                         BindingResult bindingResult, Model model) {
        log.info("Department request {}", departmentRequest);

        if (bindingResult.hasErrors()) {
            log.error("Errors: {}", bindingResult.getAllErrors().toString());
            return "views/department/department-detail";
        }


        departmentService.create(departmentRequest);

//        return "views/department/department-detail";
        return "redirect:/departments";
    }

    @GetMapping
    public String listDepartment(Model model) {

        List<DepartmentResponse> departments = departmentService.findAll();

        model.addAttribute("departments", departments);

        return "views/department/department-list";
    }

    @GetMapping("/detail")
    public String viewForm(Model model) {

        // Call department service & repo: manager list
        model.addAttribute("managers", employeeService.findAll());
        model.addAttribute("locations", List.of("17 Duy Tan, Dich Vong, Ha Noi", "234 Cau Giay, Ha Noi"));
        model.addAttribute("department", new DepartmentRequest());

        return "views/department/department-detail";
    }

    @PostMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable Long id) {
        departmentService.delete(id);
        return "redirect:/departments";
    }
}
