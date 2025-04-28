package com.example.obs.business.service.implementation;

import com.example.obs.business.service.AttendanceService;
import com.example.obs.business.requests.AttendanceCreateRequest;
import com.example.obs.business.requests.AttendanceUpdateRequest;
import com.example.obs.business.responses.AttendanceResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dateAccess.AttendanceRepository;
import com.example.obs.model.entity.Attendance;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class AttendanceServiceImpl implements AttendanceService {
    private AttendanceRepository attendanceRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<AttendanceResponse> getAll() {
        List<Attendance> attendances = attendanceRepository.findAll();

        List<AttendanceResponse> responses = attendances.stream()
                .map(attendance -> modelMapperService.forResponse()
                        .map(attendance, AttendanceResponse.class)).collect(Collectors.toList());

        return responses;
    }

    @Override
    public AttendanceResponse getById(Long id) {
        Attendance attendance = attendanceRepository.findById(id).orElseThrow();

        AttendanceResponse response = modelMapperService.forResponse()
                .map(attendance, AttendanceResponse.class);


        return response;
    }

    @Override
    public void add(AttendanceCreateRequest attendanceCreateRequest) {
        Attendance attendance = modelMapperService.forRequest()
                .map(attendanceCreateRequest, Attendance.class);

        attendanceRepository.save(attendance);
    }

    @Override
    public void update(AttendanceUpdateRequest attendanceUpdateRequest) {
        Attendance attendance = modelMapperService.forRequest()
                .map(attendanceUpdateRequest, Attendance.class);

        attendanceRepository.save(attendance);
    }

    @Override
    public void delete(Long id) {
        attendanceRepository.deleteById(id);
    }
}
