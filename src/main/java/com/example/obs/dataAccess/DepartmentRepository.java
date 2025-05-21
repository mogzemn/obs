package com.example.obs.dataAccess;

import com.example.obs.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department , Long> {
    @Query("SELECT d FROM Department d ORDER BY d.departmentCode DESC LIMIT 1")
    Optional<Department> findTopByOrderByCodeDesc();
    boolean existsByDepartmentNameIgnoreCase(String departmentName);
}
