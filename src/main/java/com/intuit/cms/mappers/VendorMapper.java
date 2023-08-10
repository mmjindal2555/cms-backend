package com.intuit.cms.mappers;

import org.springframework.stereotype.Component;

import com.intuit.cms.models.Contract;
import com.intuit.cms.models.Vendor;
import com.intuit.cms.models.dto.VendorGetDTO;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;

@Component
public class VendorMapper {
    public VendorGetDTO toDto(Vendor vendor) {
        String name = vendor.getFirstName() + " " + vendor.getLastName();
        String employeeID = vendor.getEmployee().getId().toString();
        String role = vendor.getRole();

        Contract contract = vendor.getServiceContract();
        String contractDesc = "";
        if(contract != null) {
            contractDesc = contract.getDescription();
        }


        String date = formatEpochToDate(vendor.getStartDate());

        return new VendorGetDTO(name, employeeID, role, contractDesc, vendor.getId(), date);
    }

    static String formatEpochToDate(long epochTime) {
        Instant instant = Instant.ofEpochSecond(epochTime);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, java.time.ZoneOffset.UTC);

        int day = dateTime.getDayOfMonth();
        Month month = dateTime.getMonth();
        int year = dateTime.getYear();

        return String.format("%d%s %s %d", day, getDayOfMonthSuffix(day), month, year);
    }

    private static String getDayOfMonthSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }
}
