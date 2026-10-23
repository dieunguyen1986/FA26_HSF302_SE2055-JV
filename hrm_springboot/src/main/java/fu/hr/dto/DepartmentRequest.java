package fu.hr.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

@Data
@ToString
public class DepartmentRequest {
    private  Long id;

    @NotBlank(message = "Department must be required!")
    @Pattern(regexp = "^[A-Za-z0-9 ]+$", message = "Department name is wrong format!")
    private String departmentName;

    private UUID managerId;
    private Long locationId;
    private String description;
}
