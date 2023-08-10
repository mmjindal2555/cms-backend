package com.intuit.cms.models.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class VendorAssignmentDTO {
    private Long contractID;
    private Long newServiceContractID;
    private boolean offboardingVendor;
}
