package com.intuit.cms.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intuit.cms.constants.ErrorResponses;
import com.intuit.cms.controllerAdvice.ResourceNotFoundException;
import com.intuit.cms.mappers.EmployeeMapper;
import com.intuit.cms.models.Employee;
import com.intuit.cms.models.dto.EmployeeGetDTO;
import com.intuit.cms.repositories.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeesService {

    @Autowired
    EmployeeRepository employeeRespository;

    @Autowired
    EmployeeMapper employeeMapper;

    @Override
    public EmployeeGetDTO getEmployeeByID(Long id) {
        Employee c = employeeRespository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorResponses.EMPLOYEE_404));
        return employeeMapper.toDto(c);
    }

    @Override
    public List<EmployeeGetDTO> getEmployees() {
        return employeeRespository.findAll().stream().map(employeeMapper::toDto).collect(Collectors.toList());
    }

}
