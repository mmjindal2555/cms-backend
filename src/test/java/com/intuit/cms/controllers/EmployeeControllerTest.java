package com.intuit.cms.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.intuit.cms.models.dto.EmployeeGetDTO;
import com.intuit.cms.services.EmployeesService;


public class EmployeeControllerTest {

    @Mock
    private EmployeesService service;

    @InjectMocks
    private EmployeeController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEmployeeByID() {
        EmployeeGetDTO emp = new EmployeeGetDTO();
        when(service.getEmployeeByID(1l)).thenReturn(emp);
        EmployeeGetDTO result = controller.getEmployeeByID(1l);
        assertEquals(emp, result);
    }

    @Test
    void testGetEmployees() {
        EmployeeGetDTO emp = new EmployeeGetDTO();
        List<EmployeeGetDTO> emps = new ArrayList<>();
        emps.add(emp);
        when(service.getEmployees()).thenReturn(emps);
        List<EmployeeGetDTO> result = controller.getEmployees();
        assertEquals(emps, result);
    }
}
