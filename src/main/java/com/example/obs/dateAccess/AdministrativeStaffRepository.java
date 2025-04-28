package com.example.obs.dateAccess;

import com.example.obs.model.entity.AdministrativeStaff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministrativeStaffRepository extends JpaRepository<AdministrativeStaff ,Long> {
}
