package com.example.obs.core.utilities.numbergeneration;

import com.example.obs.dataAccess.AdministrativeStaffRepository;
import com.example.obs.model.entity.AdministrativeStaff;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;

@Component
public class AdministrativeStaffNumberGenerator {

    private final AdministrativeStaffRepository staffRepository;

    public AdministrativeStaffNumberGenerator(AdministrativeStaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }


    public String generateStaffNumber(String unitName) {
        String yearCode = getYearCode();
        String unitCode = getUnitCode(unitName);
        String prefix = "AS" + yearCode + "B" + unitCode;
        String orderNumber = getNextOrderNumber(prefix);

        return prefix + orderNumber;
    }


    private String getYearCode() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return String.valueOf(currentYear).substring(2);
    }


    private String getUnitCode(String unitName) {

        return "1";
    }


    private String getNextOrderNumber(String prefix) {
        List<AdministrativeStaff> matchingStaff = staffRepository.findAll().stream()
                .filter(staff -> staff.getStaffNumber() != null && staff.getStaffNumber().startsWith(prefix))
                .toList();

        int nextOrder = matchingStaff.size() + 1;
        return String.format("%03d", nextOrder);
    }
}