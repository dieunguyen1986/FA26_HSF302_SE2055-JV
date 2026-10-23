package fu.hr.dto;

import jakarta.validation.constraints.FutureOrPresent;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class EmployeeRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    @FutureOrPresent
    private LocalDate hireDate;
    private BigDecimal salary;
    private Double commissionPct;
    private Long jobId;
    private UUID managerId;
}
