package com.intuit.cms.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class VendorGetDTO {
    private String name;
    private String employeeID;
    private String role;
    private String contract;
    private Long id;
    private String startDate;

    public VendorGetDTO(String name, String employeeID, String role, String contract, Long id, String date) {
        this.name = name;
        this.employeeID = employeeID;
        this.role = role;
        this.contract = contract;
        this.id = id;
        this.startDate = date;
    }
}
