package com.example.obs.dateAccess;

import com.example.obs.model.entity.Academic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcademicRepository extends JpaRepository<Academic, Long> {
}
