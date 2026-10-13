package fu.hr.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class DepartmentRequest {

    private  Long id;

    private String departmentName;


    private UUID managerId;
}
