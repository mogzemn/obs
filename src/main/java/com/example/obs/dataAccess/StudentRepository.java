package com.example.obs.dataAccess;

import com.example.obs.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByDepartmentIdAndStudentNumberStartingWith(Long departmentId, String studentNumberPrefix);
    
    Optional<Student> findByStudentNumber(String studentNumber);

}