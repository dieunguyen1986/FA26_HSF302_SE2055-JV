package fu.hr.controller;

import fu.hr.dto.DepartmentResponse;
import fu.hr.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping
    public String listDepartment(Model model) {

        List<DepartmentResponse> departments = departmentService.findAll();

        model.addAttribute("departments", departments);

        return "views/department_list";
    }

    @GetMapping("/detail")
    public String viewForm(Model model) {

        // Call department service & repo: manager list
        model.addAttribute("managers", List.of("Viet Anh", "Phuong"));
        model.addAttribute("locations", List.of("17 Duy Tan, Dich Vong, Ha Noi", "234 Cau Giay, Ha Noi"));

        return "views/department_detail";
    }
}
