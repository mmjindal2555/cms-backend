package com.intuit.cms.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.intuit.cms.models.dto.EmployeeGetDTO;
import com.intuit.cms.services.EmployeesService;

@RestController
public class EmployeeController {

    @Autowired
    EmployeesService service;

    @GetMapping("/employees/{id}")
    public EmployeeGetDTO getEmployeeByID(@PathVariable Long id){
        return service.getEmployeeByID(id);
    }

    @GetMapping("/employees")
    public List<EmployeeGetDTO> getEmployees(){
        return service.getEmployees();
    }

}
