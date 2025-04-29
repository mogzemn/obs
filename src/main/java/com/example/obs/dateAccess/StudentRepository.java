package com.example.obs.dateAccess;

import com.example.obs.model.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByDepartmentIdAndStudentNumberStartingWith(Long departmentId, String studentNumberPrefix);
}