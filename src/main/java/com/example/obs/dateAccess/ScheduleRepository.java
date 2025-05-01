package com.example.obs.dateAccess;

import com.example.obs.model.entity.Course;
import com.example.obs.model.entity.CourseInstructor;
import com.example.obs.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    
    // Belirli bir ders için tüm programları getir
    List<Schedule> findByCourseInstructor_Course(Course course);
    
    // Belirli bir akademik dönemde belirli bir ders için tüm programları getir
    List<Schedule> findByCourseInstructor_CourseAndAcademicTerm(Course course, String academicTerm);
    
    // Belirli bir akademik dönem ve gün için tüm programları getir
    List<Schedule> findByAcademicTermAndDay(String academicTerm, DayOfWeek day);
    
    // Belirli bir tarih için ders programını getir
    @Query("SELECT s FROM Schedule s WHERE s.academicTerm = :academicTerm AND s.day = :day AND s.isActive = true")
    List<Schedule> findSchedulesForDate(@Param("academicTerm") String academicTerm, 
                                         @Param("day") DayOfWeek day);
    
    // Belirli bir akademik dönemde, belirli bir dersin toplam ders saatini hesapla
    @Query("SELECT SUM(FUNCTION('TIMESTAMPDIFF', MINUTE, s.startTime, s.endTime)) " +
           "FROM Schedule s " +
           "WHERE s.courseInstructor.course = :course AND s.academicTerm = :academicTerm AND s.isActive = true")
    Integer calculateTotalClassHoursForCourse(@Param("course") Course course, 
                                              @Param("academicTerm") String academicTerm);
    
    // Belirli bir akademik dönemde, eğitmen için haftalık ders programını getir
    List<Schedule> findByCourseInstructor_AcademicAndAcademicTermOrderByDayAscStartTimeAsc(
            com.example.obs.model.entity.Academic academic, String academicTerm);
} 