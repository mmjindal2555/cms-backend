package com.intuit.cms.mappers;

import org.springframework.stereotype.Component;

import com.intuit.cms.models.Employee;
import com.intuit.cms.models.dto.EmployeeGetDTO;
import com.intuit.cms.models.Employee.Scope;

@Component
public class EmployeeMapper {
    public EmployeeGetDTO toDto(Employee e) {
        Long id = e.getId();
        String name = e.getName();
        String role = e.getRole();
        Scope[] scopes = e.getAccessScopes();
        return new EmployeeGetDTO(id, name, role, scopes);
    }
}

