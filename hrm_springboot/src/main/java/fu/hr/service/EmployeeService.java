package fu.hr.service;

import fu.hr.dto.EmployeeRequest;
import fu.hr.dto.EmployeeResponse;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    EmployeeResponse create(EmployeeRequest request);

    List<EmployeeResponse> findAll();

    EmployeeResponse update(UUID id, EmployeeRequest request);

    void delete(UUID id);

    EmployeeResponse findById(UUID id);

    List<EmployeeResponse> findByName(String name);

}
