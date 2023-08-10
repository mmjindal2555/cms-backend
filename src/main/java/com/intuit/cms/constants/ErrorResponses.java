package com.intuit.cms.constants;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ErrorResponses {
    public static final String EMPLOYEE_404 = "Employee Not Found";
    public static final String CONTRACT_404 = "Service Contract not found";
    public static final String VENDOR_404 = "Vendor Not Found";

    public static final String INCORRECT_MAPPING = "The Service Contract is not mapped to the vendor";
    public static final String CONTRACT_NOT_ACTIVE = "Service Contract is not ACTIVE";
    public static final String CONTRACT_NOT_APPROVED = "Only APPROVED service contracts can be made ACTIVE";
    public static final String CONTRACT_NOT_DRAFT = "Only DRAFT service contracts can be APPROVED";
    public static final String CONTRACT_CANT_BE_MADE_TO_DRAFT = "The Contract can not be rolled back to DRAFT, please create a new one";

    public static final String UNAUTHORIZED = "Not authorized to perform this action";

    public static final String DATE_TOO_OLD = "The start date is too old";
    public static final String DATE_TOO_FUTURISTIC = "The start date is too futuristic";

    public static final String INVALID_ARGUMENTS = "Passed arguments are invalid";

}
