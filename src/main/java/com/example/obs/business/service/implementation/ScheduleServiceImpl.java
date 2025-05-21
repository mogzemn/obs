package com.example.obs.business.service.implementation;

import com.example.obs.business.requests.ScheduleCreateRequest;
import com.example.obs.business.requests.ScheduleUpdateRequest;
import com.example.obs.business.responses.ScheduleResponse;
import com.example.obs.business.service.ScheduleService;
import com.example.obs.core.exceptions.BusinessException;
import com.example.obs.core.exceptions.NotFoundException;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dataAccess.CourseInstructorRepository;
import com.example.obs.dataAccess.CourseRepository;
import com.example.obs.dataAccess.ScheduleRepository;
import com.example.obs.model.entity.Course;
import com.example.obs.model.entity.CourseInstructor;
import com.example.obs.model.entity.Schedule;
import lombok.AllArgsConstructor;
import org.hibernate.StaleObjectStateException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ModelMapperService modelMapperService;
    private final CourseInstructorRepository courseInstructorRepository;
    private final CourseRepository courseRepository;

    @Override
    public ScheduleResponse add(ScheduleCreateRequest createRequest) {
        try {
            CourseInstructor courseInstructor = courseInstructorRepository.findById(createRequest.getCourseInstructorId())
                    .orElseThrow(() -> new NotFoundException("Ders Eğitmeni bulunamadı! ID: " + createRequest.getCourseInstructorId()));
            
            Course course = courseRepository.findById(createRequest.getCourseId())
                    .orElseThrow(() -> new NotFoundException("Ders bulunamadı! ID: " + createRequest.getCourseId()));

            Schedule schedule = modelMapperService.forRequest().map(createRequest, Schedule.class);
            schedule.setCourseInstructor(courseInstructor);
            schedule.setCourse(course); 

            scheduleRepository.save(schedule);
            return mapToResponse(schedule);
        } catch (Exception e) {
            throw new BusinessException("Ders programı eklenirken bir hata oluştu: " + e.getMessage());
        }
    }

    @Override
    public ScheduleResponse update(ScheduleUpdateRequest updateRequest) {
        try {
            Schedule scheduleToUpdate = scheduleRepository.findById(updateRequest.getId())
                    .orElseThrow(() -> new NotFoundException("Ders programı kaydı bulunamadı! ID: " + updateRequest.getId()));

            CourseInstructor courseInstructor = courseInstructorRepository.findById(updateRequest.getCourseInstructorId())
                    .orElseThrow(() -> new NotFoundException("Ders Eğitmeni bulunamadı! ID: " + updateRequest.getCourseInstructorId()));
            
            Course course = courseRepository.findById(updateRequest.getCourseId())
                    .orElseThrow(() -> new NotFoundException("Ders bulunamadı! ID: " + updateRequest.getCourseId()));

            modelMapperService.forRequest().map(updateRequest, scheduleToUpdate);
            scheduleToUpdate.setCourse(course);
            scheduleToUpdate.setCourseInstructor(courseInstructor);

            scheduleRepository.save(scheduleToUpdate);
            return mapToResponse(scheduleToUpdate);
        } catch (OptimisticLockingFailureException | StaleObjectStateException e) {
            Schedule freshSchedule = scheduleRepository.findById(updateRequest.getId())
                    .orElseThrow(() -> new NotFoundException("Ders programı kaydı bulunamadı! ID: " + updateRequest.getId()));
            
            CourseInstructor courseInstructor = courseInstructorRepository.findById(updateRequest.getCourseInstructorId())
                    .orElseThrow(() -> new NotFoundException("Ders Eğitmeni bulunamadı! ID: " + updateRequest.getCourseInstructorId()));

            Course freshCourse = courseRepository.findById(updateRequest.getCourseId())
                    .orElseThrow(() -> new NotFoundException("Ders bulunamadı! ID: " + updateRequest.getCourseId()));

            modelMapperService.forRequest().map(updateRequest, freshSchedule);
            freshSchedule.setCourse(freshCourse); 
            freshSchedule.setCourseInstructor(courseInstructor);
            
            scheduleRepository.save(freshSchedule);
            return mapToResponse(freshSchedule);
        } catch (Exception e) {
            throw new BusinessException("Ders programı güncellenirken bir hata oluştu: " + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        try {
            if (!scheduleRepository.existsById(id)) {
                throw new NotFoundException("Silme işlemi başarısız. ID: " + id + " ile eşleşen bir ders programı kaydı bulunamadı.");
            }
            scheduleRepository.deleteById(id);
        } catch (Exception e) {
            if (e instanceof NotFoundException) {
                throw e;
            }
            throw new BusinessException("Ders programı silinirken bir hata oluştu: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResponse> getAll() {
        List<Schedule> schedules = scheduleRepository.findAll();
        return schedules.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ScheduleResponse getById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ders programı kaydı bulunamadı! ID: " + id));
        return mapToResponse(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResponse> getByCourseId(Long courseId) {
        List<Schedule> schedules = scheduleRepository.findByCourseId(courseId);
        return schedules.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ScheduleResponse> getByCourseInstructorId(Long courseInstructorId) {
        List<Schedule> schedules = scheduleRepository.findByCourseInstructorId(courseInstructorId);
        return schedules.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ScheduleResponse mapToResponse(Schedule schedule) {
        ScheduleResponse response = modelMapperService.forResponse().map(schedule, ScheduleResponse.class);
        if (schedule.getCourseInstructor() != null) {
            response.setCourseInstructorId(schedule.getCourseInstructor().getId());
            if (schedule.getCourseInstructor().getAcademic() != null && schedule.getCourseInstructor().getAcademic().getUser() != null) {
                response.setAcademicFullName(schedule.getCourseInstructor().getAcademic().getUser().getFirstName()
                        + " " + schedule.getCourseInstructor().getAcademic().getUser().getLastName());
            }
        }
        if (schedule.getCourse() != null) {
            response.setCourseName(schedule.getCourse().getCourseName());
        }
        return response;
    }
}
