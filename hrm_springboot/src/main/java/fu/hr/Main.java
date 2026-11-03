package fu.hr;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Main {
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("12345"));
    }
}
