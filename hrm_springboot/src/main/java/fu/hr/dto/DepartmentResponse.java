package fu.hr.dto;

import lombok.Data;


@Data
public class DepartmentResponse {
    private  Long id;

    private String departmentName;

    private String managerName;
    private String description;
}
