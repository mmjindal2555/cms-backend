package com.intuit.cms.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter @Setter @NoArgsConstructor
public class VendorUpsertDTO {
    private String firstName;
    private String lastName;
    private String role;
    private long startDate;

    public VendorUpsertDTO(String firstName, String lastName, String role, long startDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.startDate = startDate;
    }

    public String getFullName(){
        return this.firstName+" "+this.lastName;
    }
}
