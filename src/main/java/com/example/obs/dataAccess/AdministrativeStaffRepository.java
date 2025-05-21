package com.example.obs.dataAccess;

import com.example.obs.model.entity.AdministrativeStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministrativeStaffRepository extends JpaRepository<AdministrativeStaff, Long> {
    Optional<AdministrativeStaff> findByStaffNumber(String staffNumber);
    
    boolean existsByStaffNumber(String staffNumber);
    
    Optional<AdministrativeStaff> findByUser_Id(Long userId);
}
