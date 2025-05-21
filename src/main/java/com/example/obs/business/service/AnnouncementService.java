package com.example.obs.business.service;

import com.example.obs.business.requests.AnnouncementCreateRequest;
import com.example.obs.business.requests.AnnouncementUpdateRequest;
import com.example.obs.business.responses.AnnouncementResponse;

import java.util.List;

public interface AnnouncementService {
    AnnouncementResponse add(AnnouncementCreateRequest request);
    AnnouncementResponse update(AnnouncementUpdateRequest request);
    void delete(Long id);
    AnnouncementResponse getById(Long id);
    List<AnnouncementResponse> getAll();
    List<AnnouncementResponse> getAllActive();
}
