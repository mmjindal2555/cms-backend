package com.intuit.cms.models.dto;

import com.intuit.cms.models.Employee.Scope;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class EmployeeGetDTO {
    private String name;
    private String role;
    private Long id;
    private Scope[] scopes;

    public EmployeeGetDTO(Long id, String name, String role, Scope[] scopes) {
        this.name = name;
        this.role = role;
        this.id = id;
        this.scopes = scopes;
    }

}
