package fu.hr.service;

import fu.hr.dto.DepartmentRequest;
import fu.hr.dto.DepartmentResponse;
import fu.hr.entity.Department;
import fu.hr.entity.Employees;
import fu.hr.repository.DepartmentRepository;
import fu.hr.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public DepartmentResponse create(DepartmentRequest request) {

        if (request.getDepartmentName() == null || request.getDepartmentName().trim().equals("")) {
            throw new IllegalArgumentException("Department name is invalid (cannot be empty)!");
        }

        if (departmentRepository.existsByDepartmentName(request.getDepartmentName())) {
            throw new IllegalArgumentException("Department already exists!");
        }

        Employees employee = employeeRepository.findById(request.getManagerId()).orElse(null); // E10


        // Map dto to entity

        Department department = new Department();
        department.setDepartmentName(request.getDepartmentName());

        if (employee != null) {
            department.setDeptManager(employee);
        }
        department.setDescription(request.getDescription());

        Department deptEntity = departmentRepository.save(department);

        return toDto(deptEntity);
    }

    @Override
    @Transactional
    public List<DepartmentResponse> findAll() {

        return departmentRepository.findAll().stream().map((department) -> toDto(department)).toList();
    }

    @Override
    public DepartmentResponse update(DepartmentRequest request) {
        return null;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (id == null || !departmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Department not found!");
        }

        departmentRepository.deleteById(id);
    }

    @Override
    @Transactional
    public DepartmentResponse findById(Long id) { // Lambda Expression
        Department department = departmentRepository.findById(id).orElseThrow(() -> {
            return new RuntimeException("Department not found!");
        });
        return toDto(department);
    }

    @Override
    @Transactional
    public List<DepartmentResponse> findByName(String name) {
        return departmentRepository.findByDepartmentNameContaining(name).stream().map((department) -> toDto(department)).toList();
    }

    private DepartmentResponse toDto(Department department) {
        DepartmentResponse response = new DepartmentResponse();
        response.setId(department.getId());
        response.setDepartmentName(department.getDepartmentName());
        if (department.getDeptManager() != null) {
            response.setManagerName(department.getDeptManager().getFirstName() + " "
                    + department.getDeptManager().getLastName());
        }


        response.setDescription(department.getDescription());

        return response;
    }
}

//class SupplierImpl implements Supplier {
//    @Override
//    public RuntimeException get() {
//        return new RuntimeException("Department not found Exception!");
//    }
//}
