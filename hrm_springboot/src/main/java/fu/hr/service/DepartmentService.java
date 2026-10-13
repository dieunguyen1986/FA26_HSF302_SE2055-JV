package fu.hr.service;

import fu.hr.dto.DepartmentRequest;
import fu.hr.dto.DepartmentResponse;

import java.util.List;

public interface DepartmentService {
     DepartmentResponse create(DepartmentRequest request);

     List<DepartmentResponse> findAll();
     DepartmentResponse update(DepartmentRequest request);

     void delete(Long id);

     DepartmentResponse findById(Long id);

     List<DepartmentResponse> findByName(String name);
}
