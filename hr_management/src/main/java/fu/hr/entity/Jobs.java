package fu.hr.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jobs", check = {
        @CheckConstraint(name = "CHK_MIN", constraint = "min_salary >= 0"),
        @CheckConstraint(name = "CHK_MIN_MAX", constraint = "min_salary < max_salary")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Jobs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_title", unique = true, nullable = false)
    private String jobTitle;

    @Column(name = "min_salary", columnDefinition = "DECIMAL(11,2)")
    private Double minSalary;


    @Column(name = "max_salary", columnDefinition = "DECIMAL(11,2)")
    private Double maxSalary;

}
