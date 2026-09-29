package fu.hr.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        System.err.println("Exception Handling: " + e.getMessage());
        e.printStackTrace();
        return e.getMessage();
    }
}
