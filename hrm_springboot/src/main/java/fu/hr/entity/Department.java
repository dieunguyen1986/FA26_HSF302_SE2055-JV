package fu.hr.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "departments")
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(name = "department_name")
    private String departmentName;

    @OneToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private Employees deptManager;

    private String description;

}
