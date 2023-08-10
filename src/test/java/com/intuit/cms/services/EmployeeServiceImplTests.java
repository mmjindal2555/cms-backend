package com.intuit.cms.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.intuit.cms.mappers.EmployeeMapper;
import com.intuit.cms.mappers.VendorMapper;
import com.intuit.cms.models.Employee;
import com.intuit.cms.models.Vendor;
import com.intuit.cms.models.dto.EmployeeGetDTO;
import com.intuit.cms.models.dto.VendorGetDTO;
import com.intuit.cms.repositories.ContractRepository;
import com.intuit.cms.repositories.EmployeeRepository;
import com.intuit.cms.repositories.VendorRepository;

@SpringJUnitConfig
@SpringBootTest
@ActiveProfiles("test")
public class EmployeeServiceImplTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private VendorRepository vendorRepository;

    @Autowired
    EmployeeMapper mapper;

    @InjectMocks
    private EmployeeServiceImpl service;

    @Test
    void testGetEmployeeByID() {
        service.employeeMapper = mapper;
        Employee e = new Employee("manish", "Admin");
        e.setId(1L);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(e));
        EmployeeGetDTO result = service.getEmployeeByID(1l);
        assertNotNull(result);
    }

    @Test
    void testGetEmployees() {
        service.employeeMapper = mapper;
        Employee e = new Employee("manish", "Admin");
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(new Employee[]{e}));
        List<EmployeeGetDTO> result = service.getEmployees();
        assertNotNull(result);
    }
}
