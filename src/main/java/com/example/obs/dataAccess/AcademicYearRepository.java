package com.example.obs.dataAccess;

import com.example.obs.model.entity.AcademicYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {
    Optional<AcademicYear> findByName(String name);
    List<AcademicYear> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate date, LocalDate sameDate);
    default Optional<AcademicYear> findByDate(LocalDate date) {
        List<AcademicYear> academicYears = findByStartDateLessThanEqualAndEndDateGreaterThanEqual(date, date);
        return academicYears.isEmpty() ? Optional.empty() : Optional.of(academicYears.get(0));
    }
    @Query("SELECT a FROM AcademicYear a WHERE YEAR(a.startDate) = :startYear AND YEAR(a.endDate) = :endYear")
    Optional<AcademicYear> findByStartYearAndEndYear(@Param("startYear") int startYear, @Param("endYear") int endYear);
}
