package com.example.obs.business.service.implementation;

import com.example.obs.business.requests.AnnouncementCreateRequest;
import com.example.obs.business.requests.AnnouncementUpdateRequest;
import com.example.obs.business.responses.AnnouncementResponse;
import com.example.obs.business.service.AnnouncementService;
import com.example.obs.core.exceptions.NotFoundException;
import com.example.obs.core.utilities.mappers.ModelMapperService;
import com.example.obs.dataAccess.AnnouncementRepository;
import com.example.obs.model.entity.Announcement;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;
    private final ModelMapperService modelMapperService;

    @Override
    public AnnouncementResponse add(AnnouncementCreateRequest request) {
        Announcement announcement = modelMapperService.forRequest().map(request, Announcement.class);
        if (request.getPublishedDate() == null) {
            announcement.setPublishedDate(LocalDateTime.now());
        }
        if (request.getIsActive() == null) {
            announcement.setIsActive(true);
        }


        announcement = announcementRepository.save(announcement);
        return mapToResponse(announcement);
    }

    @Override
    public AnnouncementResponse update(AnnouncementUpdateRequest request) {
        Announcement existingAnnouncement = announcementRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Duyuru bulunamadı ID: " + request.getId()));

        if (request.getTitle() != null) {
            existingAnnouncement.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            existingAnnouncement.setContent(request.getContent());
        }
        if (request.getPublishedDate() != null) {
            existingAnnouncement.setPublishedDate(request.getPublishedDate());
        }
        if (request.getIsActive() != null) {
            existingAnnouncement.setIsActive(request.getIsActive());
        }

        existingAnnouncement = announcementRepository.save(existingAnnouncement);
        return mapToResponse(existingAnnouncement);
    }

    @Override
    public void delete(Long id) {
        if (!announcementRepository.existsById(id)) {
            throw new NotFoundException("Silinecek duyuru bulunamadı ID: " + id);
        }
        announcementRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public AnnouncementResponse getById(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Duyuru bulunamadı ID: " + id));
        return mapToResponse(announcement);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnouncementResponse> getAll() {
        return announcementRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnouncementResponse> getAllActive() {
        return announcementRepository.findByIsActiveTrueOrderByPublishedDateDesc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private AnnouncementResponse mapToResponse(Announcement announcement) {
        AnnouncementResponse response = modelMapperService.forResponse().map(announcement, AnnouncementResponse.class);
        return response;
    }
}
