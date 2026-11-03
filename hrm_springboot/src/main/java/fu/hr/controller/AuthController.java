package fu.hr.controller;

import fu.hr.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/", "/auths"})
public class AuthController {

    // username,

//    @PostMapping
//    public String login(HttpSession session, HttpServletRequest request) {
//
//        session.setAttribute("user", request.getSession().getAttribute("user"));
//
//        return "/views/department/department-list";
//    }

    @GetMapping({"/", "/login"})
    public String loginForm(Model model) {

        model.addAttribute("loginRequest", new LoginRequest());

        return "views/auth/login";
    }
}
