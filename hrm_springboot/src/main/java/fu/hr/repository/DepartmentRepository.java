package fu.hr.repository;

import fu.hr.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByDepartmentName(@Param("departmentName") String departmentName);
    // departmentName LIKE :departmentName
    List<Department> findByDepartmentNameContaining(@Param("departmentName") String departmentName);
}
