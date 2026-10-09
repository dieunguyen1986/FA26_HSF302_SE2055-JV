package fu.hr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    @GetMapping
    public String view(){
        return "views/department_list";
    }

    @GetMapping("/detail")
    public  String viewForm(Model model){

        // Call department service & repo: manager list
        model.addAttribute("managers", List.of("Viet Anh", "Phuong"));
        model.addAttribute("locations", List.of("17 Duy Tan, Dich Vong, Ha Noi", "234 Cau Giay, Ha Noi"));

        return "views/department_detail";
    }
}
