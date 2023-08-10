package com.intuit.cms.services;

import java.util.List;

import com.intuit.cms.models.dto.EmployeeGetDTO;

public interface EmployeesService {
    public EmployeeGetDTO getEmployeeByID(Long id);
    public List<EmployeeGetDTO> getEmployees();
}
