package fu.hr.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JobResponse {
    private Long id;
    private String jobTitle;

    private Double minSalary;

    private Double maxSalary;

}
