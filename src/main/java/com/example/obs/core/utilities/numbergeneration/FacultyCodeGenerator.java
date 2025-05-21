package com.example.obs.core.utilities.numbergeneration;

import com.example.obs.dataAccess.FacultyRepository;
import com.example.obs.model.entity.Faculty;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class FacultyCodeGenerator {

    private final FacultyRepository facultyRepository;

    public String generateNextCode() {
        Optional<Faculty> lastFaculty = facultyRepository.findTopByOrderByCodeDesc();
        if (lastFaculty.isPresent()) {
            String lastCode = lastFaculty.get().getFacultyCode();
            int lastNumber = Integer.parseInt(lastCode);
            int nextNumber = lastNumber + 1;
            return String.format("%02d", nextNumber);
        } else {
            return "01";
        }
    }
}
