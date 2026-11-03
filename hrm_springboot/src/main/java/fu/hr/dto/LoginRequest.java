package fu.hr.dto;

import lombok.*;

@NoArgsConstructor@AllArgsConstructor
@Setter@Getter
@ToString
public class LoginRequest {
    private  String email;
    private String password;

}
