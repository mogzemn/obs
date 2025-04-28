package com.example.obs.business.concretes;

import com.example.obs.business.abstracts.AttendanceService;
import com.example.obs.business.requests.AttendanceCreateRequest;
import com.example.obs.business.requests.AttendanceUpdateRequest;
import com.example.obs.business.responses.AttendanceResponse;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dateAccess.AttendanceRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor

public class AttendanceManager implements AttendanceService {
    private AttendanceRepository attendanceRepository;
    private ModelMapperService modelMapperService;

    @Override
    public List<AttendanceResponse> getAll() {
        return List.of();
    }

    @Override
    public AttendanceResponse getById(Long id) {
        return null;
    }

    @Override
    public void add(AttendanceCreateRequest attendanceCreateRequest) {

    }

    @Override
    public void update(AttendanceUpdateRequest attendanceUpdateRequest) {

    }

    @Override
    public void delete(Long id) {

    }
}
