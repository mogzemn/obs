package com.example.obs.webApi.controllers;

import com.example.obs.business.requests.AdministrativeStaffCreateRequest;
import com.example.obs.business.requests.AdministrativeStaffUpdateRequest;
import com.example.obs.business.responses.AdministrativeStaffResponse;
import com.example.obs.business.service.AdministrativeStaffService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/administrative-staffs")
@AllArgsConstructor
public class AdministrativeStaffsController {

    private AdministrativeStaffService administrativeStaffService;

    @GetMapping("/all")
    public List<AdministrativeStaffResponse> getAll() {
        return administrativeStaffService.getAll();
    }

    @GetMapping("/{id}")
    public AdministrativeStaffResponse getById(@PathVariable Long id) {
        return administrativeStaffService.getById(id);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public void add(@RequestBody AdministrativeStaffCreateRequest administrativeStaffCreateRequest) {
        this.administrativeStaffService.add(administrativeStaffCreateRequest);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody AdministrativeStaffUpdateRequest administrativeStaffUpdateRequest) {
        administrativeStaffUpdateRequest.setId(id);
        this.administrativeStaffService.update(administrativeStaffUpdateRequest);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        this.administrativeStaffService.delete(id);
    }
}