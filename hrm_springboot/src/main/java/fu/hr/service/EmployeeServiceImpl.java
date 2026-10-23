package fu.hr.service;

import fu.hr.dto.EmployeeRequest;
import fu.hr.dto.EmployeeResponse;
import fu.hr.entity.Employees;
import fu.hr.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository; // Injection

    @Override
    public EmployeeResponse create(EmployeeRequest request) {
        return null;
    }

    @Override
    public List<EmployeeResponse> findAll() {
        List<Employees> employees = employeeRepository.findAll();
        List<EmployeeResponse> employeeResponses = employees.stream().map((employee) -> toDto(employee)).toList();
        return employeeResponses;
    }

    @Override
    public EmployeeResponse update(UUID id, EmployeeRequest request) {
        return null;
    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public EmployeeResponse findById(UUID id) {
        return null;
    }

    @Override
    public List<EmployeeResponse> findByName(String name) {
        return List.of();
    }


    private EmployeeResponse toDto(Employees employee) {
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setId(employee.getId());
        employeeResponse.setFirstName(employee.getFirstName());
        employeeResponse.setLastName(employee.getLastName());
        employeeResponse.setEmail(employee.getEmail());
        employeeResponse.setCommissionPct(employee.getCommissionPct());
        employeeResponse.setSalary(employee.getSalary());
        if (employee.getJob() != null) {
            employeeResponse.setJobId(employee.getJob().getId());
            employeeResponse.setJobTitle(employee.getJob().getJobTitle());
        }
        if (employee.getManager() != null) {
            employeeResponse.setManagerId(employee.getManager().getId());
        }


        return employeeResponse;
    }
}
