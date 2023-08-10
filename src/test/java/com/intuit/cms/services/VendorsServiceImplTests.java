package com.intuit.cms.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.ArrayList;
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

import com.intuit.cms.constants.SystemConstants;
import com.intuit.cms.controllerAdvice.RequestException;
import com.intuit.cms.controllerAdvice.ResourceNotFoundException;
import com.intuit.cms.controllerAdvice.UnauthorizedException;
import com.intuit.cms.mappers.VendorMapper;
import com.intuit.cms.models.Contract;
import com.intuit.cms.models.Employee;
import com.intuit.cms.models.Vendor;
import com.intuit.cms.models.Contract.State;
import com.intuit.cms.models.Employee.Scope;
import com.intuit.cms.models.dto.VendorAssignmentDTO;
import com.intuit.cms.models.dto.VendorGetDTO;
import com.intuit.cms.models.dto.VendorUpsertDTO;
import com.intuit.cms.repositories.ContractRepository;
import com.intuit.cms.repositories.EmployeeRepository;
import com.intuit.cms.repositories.VendorRepository;

@SpringJUnitConfig
@SpringBootTest
@ActiveProfiles("test")
public class VendorsServiceImplTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private VendorRepository vendorRepository;

    @Autowired
    VendorMapper mapper;

    @InjectMocks
    private VendorsServiceImpl service;

    @Test
    void testCreateVendorAdmin_DATE_TOO_OLD() {
        service.vendorMapper = mapper;
        Employee adminEmployee = new Employee();
        adminEmployee.setId(1L);
        adminEmployee.setRole("Administrator");
        adminEmployee.setAccessScopes(new Employee.Scope[]{Employee.Scope.ADMIN});

        VendorUpsertDTO contractDto = VendorUpsertDTO.builder()
            .firstName("Test")
            .lastName("user")
            .role("Product Manager")
            .build();
        Employee e = new Employee();
        e.setName("Test user");
        e.setAccessScopes(new Scope[]{Scope.VENDOR});
        e.setRole("Product Manager");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(adminEmployee));

        Vendor v = new Vendor();
        v.setFirstName("Test");
        v.setLastName("user");
        v.setRole("Product Manager");
        v.setEmployee(e);

        when(vendorRepository.save(any(Vendor.class))).thenReturn(v);
        assertThrows(RequestException.class, () -> service.createVendor(contractDto, 1L));
    }

    @Test
    void testCreateVendorAdmin_DATE_TOO_FUTURISTIC() {
        service.vendorMapper = mapper;
        Employee adminEmployee = new Employee();
        adminEmployee.setId(1L);
        adminEmployee.setRole("Administrator");
        adminEmployee.setAccessScopes(new Employee.Scope[]{Employee.Scope.ADMIN});
        int date = (int)Instant.now().getEpochSecond() + (SystemConstants.A_YEARS_TIME * 2);
        VendorUpsertDTO contractDto = VendorUpsertDTO.builder()
            .firstName("Test")
            .lastName("user")
            .role("Product Manager")
            .startDate(date)
            .build();
        Employee e = new Employee();
        e.setName("Test user");
        e.setAccessScopes(new Scope[]{Scope.VENDOR});
        e.setRole("Product Manager");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(adminEmployee));

        Vendor v = new Vendor();
        v.setFirstName("Test");
        v.setLastName("user");
        v.setRole("Product Manager");
        v.setStartDate(date);
        v.setEmployee(e);

        when(vendorRepository.save(any(Vendor.class))).thenReturn(v);
        assertThrows(RequestException.class, () -> service.createVendor(contractDto, 1L));
    }

    @Test
    void testCreateVendorAdmin() {
        service.vendorMapper = mapper;
        Employee adminEmployee = new Employee();
        adminEmployee.setId(1L);
        adminEmployee.setRole("Administrator");
        adminEmployee.setAccessScopes(new Employee.Scope[]{Employee.Scope.ADMIN});

        int date = (int)Instant.now().getEpochSecond();
        VendorUpsertDTO vendorUpsertDTO = VendorUpsertDTO.builder()
            .firstName("Test")
            .lastName("user")
            .role("Product Manager")
            .startDate(date)
            .build();
        Employee e = new Employee();
        e.setId(67L);
        e.setName("Test user");
        e.setAccessScopes(new Scope[]{Scope.VENDOR});
        e.setRole("Product Manager");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(adminEmployee));

        Vendor v = new Vendor();
        v.setFirstName("Test");
        v.setLastName("user");
        v.setRole("Product Manager");
        v.setStartDate(date);
        v.setEmployee(e);

        when(vendorRepository.save(any(Vendor.class))).thenReturn(v);

        VendorGetDTO result =  service.createVendor(vendorUpsertDTO, 1L);
        assertNotNull(result);
    }

    @Test
    void testCreateVendor_NOT_Admin() {
        service.vendorMapper = mapper;
        Employee adminEmployee = new Employee();
        adminEmployee.setId(1L);
        adminEmployee.setRole("Administrator");
        adminEmployee.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        int date = (int)Instant.now().getEpochSecond();
        VendorUpsertDTO vendorUpsertDTO = VendorUpsertDTO.builder()
            .firstName("Test")
            .lastName("user")
            .role("Product Manager")
            .startDate(date)
            .build();
        Employee e = new Employee();
        e.setId(67L);
        e.setName("Test user");
        e.setAccessScopes(new Scope[]{Scope.VENDOR});
        e.setRole("Product Manager");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(adminEmployee));

        Vendor v = new Vendor();
        v.setFirstName("Test");
        v.setLastName("user");
        v.setRole("Product Manager");
        v.setStartDate(date);
        v.setEmployee(e);

        when(vendorRepository.save(any(Vendor.class))).thenReturn(v);

        assertThrows(UnauthorizedException.class, () -> service.createVendor(vendorUpsertDTO, 1L));
    }

    @Test
    void testAssignContractToVendor_SUCCESS() {
        service.vendorMapper = mapper;
        Employee adminEmployee = new Employee();
        adminEmployee.setId(1L);
        adminEmployee.setRole("Administrator");
        adminEmployee.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        int date = (int)Instant.now().getEpochSecond();
        VendorAssignmentDTO vendorAssignmentDTO = new VendorAssignmentDTO();
        vendorAssignmentDTO.setOffboardingVendor(false);
        vendorAssignmentDTO.setNewServiceContractID(34L);

        Employee e = new Employee();
        e.setId(67L);
        e.setName("Test user");
        e.setAccessScopes(new Scope[]{Scope.VENDOR});
        e.setRole("Product Manager");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(adminEmployee));

        Vendor v = new Vendor();
        v.setFirstName("Test");
        v.setLastName("user");
        v.setRole("Product Manager");
        v.setStartDate(date);
        v.setEmployee(e);

        Contract c = new Contract();
        c.setState(State.DRAFT);
        c.setId(34L);
        c.setServiceContractOwner(adminEmployee);
        c.setState(State.ACTIVE);
        c.setDescription("MNO");

        when(contractRepository.findById(34L)).thenReturn(Optional.of(c));
        when(vendorRepository.findById(2L)).thenReturn(Optional.of(v));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(v);

        VendorGetDTO result = service.updateVendorAssignment(vendorAssignmentDTO, 2L, 1L);
        assertNotNull(result);
    }

    @Test
    void testAssignContractToVendor_SERVICE_CONTRACT_NOT_ACTIVE() {
        service.vendorMapper = mapper;
        Employee adminEmployee = new Employee();
        adminEmployee.setId(1L);
        adminEmployee.setRole("Administrator");
        adminEmployee.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        int date = (int)Instant.now().getEpochSecond();
        VendorAssignmentDTO vendorAssignmentDTO = new VendorAssignmentDTO();
        vendorAssignmentDTO.setOffboardingVendor(false);
        vendorAssignmentDTO.setNewServiceContractID(34L);

        Employee e = new Employee();
        e.setId(67L);
        e.setName("Test user");
        e.setAccessScopes(new Scope[]{Scope.VENDOR});
        e.setRole("Product Manager");

        when(employeeRepository.findById(67L)).thenReturn(Optional.of(adminEmployee));

        Vendor v = new Vendor();
        v.setFirstName("Test");
        v.setLastName("user");
        v.setRole("Product Manager");
        v.setStartDate(date);
        v.setEmployee(e);

        Contract c = new Contract();
        c.setState(State.DRAFT);
        c.setId(34L);
        c.setServiceContractOwner(e);
        c.setDescription("MNO");

        when(contractRepository.findById(34L)).thenReturn(Optional.of(c));
        when(vendorRepository.findById(2L)).thenReturn(Optional.of(v));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(v);

        assertThrows(RequestException.class, () -> service.updateVendorAssignment(vendorAssignmentDTO, 2L, 67L));
    }

    @Test
    void testAssignContractToVendor_UNAUTHORIZED_DIFFERENT_SERVICE_OWNER() {
        service.vendorMapper = mapper;
        Employee adminEmployee = new Employee();
        adminEmployee.setId(1L);
        adminEmployee.setRole("Administrator");
        adminEmployee.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        int date = (int)Instant.now().getEpochSecond();
        VendorAssignmentDTO vendorAssignmentDTO = new VendorAssignmentDTO();
        vendorAssignmentDTO.setOffboardingVendor(false);
        vendorAssignmentDTO.setNewServiceContractID(34L);

        Employee e = new Employee();
        e.setId(67L);
        e.setName("Test user");
        e.setAccessScopes(new Scope[]{Scope.VENDOR});
        e.setRole("Product Manager");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(adminEmployee));

        Vendor v = new Vendor();
        v.setFirstName("Test");
        v.setLastName("user");
        v.setRole("Product Manager");
        v.setStartDate(date);
        v.setEmployee(e);

        Contract c = new Contract();
        c.setState(State.DRAFT);
        c.setId(34L);
        c.setServiceContractOwner(e);
        c.setDescription("MNO");

        when(contractRepository.findById(34L)).thenReturn(Optional.of(c));
        when(vendorRepository.findById(2L)).thenReturn(Optional.of(v));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(v);

        assertThrows(UnauthorizedException.class, () -> service.updateVendorAssignment(vendorAssignmentDTO, 2L, 1L));
    }

    @Test
    void testAssignContractToVendor_NOT_UNAUTHORIZED_CAUSE_ADMIN() {
        service.vendorMapper = mapper;
        Employee adminEmployee = new Employee();
        adminEmployee.setId(1L);
        adminEmployee.setRole("Administrator");
        adminEmployee.setAccessScopes(new Employee.Scope[]{Employee.Scope.ADMIN});

        int date = (int)Instant.now().getEpochSecond();
        VendorAssignmentDTO vendorAssignmentDTO = new VendorAssignmentDTO();
        vendorAssignmentDTO.setOffboardingVendor(false);
        vendorAssignmentDTO.setNewServiceContractID(34L);

        Employee e = new Employee();
        e.setId(67L);
        e.setName("Test user");
        e.setAccessScopes(new Scope[]{Scope.VENDOR});
        e.setRole("Product Manager");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(adminEmployee));

        Contract c = new Contract();
        c.setState(State.ACTIVE);
        c.setId(34L);
        c.setServiceContractOwner(e);
        c.setDescription("MNO");

        Vendor v = new Vendor();
        v.setFirstName("Test");
        v.setLastName("user");
        v.setRole("Product Manager");
        v.setStartDate(date);
        v.setServiceContract(c);
        v.setEmployee(e);

        when(contractRepository.findById(34L)).thenReturn(Optional.of(c));
        when(vendorRepository.findById(2L)).thenReturn(Optional.of(v));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(v);
        VendorGetDTO ven = service.updateVendorAssignment(vendorAssignmentDTO, 2L, 1L);
        assertNotNull(ven);

    }

    @Test
    void testAssignContractToVendor_INCORRECT_MAPPING() {
        service.vendorMapper = mapper;
        Employee adminEmployee = new Employee();
        adminEmployee.setId(1L);
        adminEmployee.setRole("Administrator");
        adminEmployee.setAccessScopes(new Employee.Scope[]{Employee.Scope.ADMIN});

        int date = (int)Instant.now().getEpochSecond();
        VendorAssignmentDTO vendorAssignmentDTO = new VendorAssignmentDTO();
        vendorAssignmentDTO.setContractID(20L);
        vendorAssignmentDTO.setOffboardingVendor(false);
        vendorAssignmentDTO.setNewServiceContractID(34L);

        Employee e = new Employee();
        e.setId(67L);
        e.setName("Test user");
        e.setAccessScopes(new Scope[]{Scope.VENDOR});
        e.setRole("Product Manager");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(adminEmployee));

        Contract c = new Contract();
        c.setState(State.ACTIVE);
        c.setId(34L);
        c.setServiceContractOwner(e);
        c.setDescription("MNO");

        Vendor v = new Vendor();
        v.setFirstName("Test");
        v.setLastName("user");
        v.setRole("Product Manager");
        v.setStartDate(date);
        v.setServiceContract(c);
        v.setEmployee(e);

        when(contractRepository.findById(34L)).thenReturn(Optional.of(c));
        when(vendorRepository.findById(2L)).thenReturn(Optional.of(v));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(v);

        assertThrows(RequestException.class, () -> service.updateVendorAssignment(vendorAssignmentDTO, 2L, 1L));
    }

    @Test
    void testAssignContractToVendor_CONTRACT_NOT_FOUND() {
        service.vendorMapper = mapper;
        Employee adminEmployee = new Employee();
        adminEmployee.setId(1L);
        adminEmployee.setRole("Administrator");
        adminEmployee.setAccessScopes(new Employee.Scope[]{Employee.Scope.ADMIN});

        int date = (int)Instant.now().getEpochSecond();
        VendorAssignmentDTO vendorAssignmentDTO = new VendorAssignmentDTO();
        vendorAssignmentDTO.setOffboardingVendor(false);
        vendorAssignmentDTO.setNewServiceContractID(34L);

        Employee e = new Employee();
        e.setId(67L);
        e.setName("Test user");
        e.setAccessScopes(new Scope[]{Scope.VENDOR});
        e.setRole("Product Manager");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(adminEmployee));

        Vendor v = new Vendor();
        v.setFirstName("Test");
        v.setLastName("user");
        v.setRole("Product Manager");
        v.setStartDate(date);
        v.setEmployee(e);

        when(vendorRepository.findById(2L)).thenReturn(Optional.of(v));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(v);
        assertThrows(ResourceNotFoundException.class, () -> service.updateVendorAssignment(vendorAssignmentDTO, 2L, 1L));
    }

    @Test
    void testUnAssignContractToVendor_SUCCESS() {
        service.vendorMapper = mapper;
        Employee adminEmployee = new Employee();
        adminEmployee.setId(1L);
        adminEmployee.setRole("Administrator");
        adminEmployee.setAccessScopes(new Employee.Scope[]{Employee.Scope.ADMIN});

        int date = (int)Instant.now().getEpochSecond();
        VendorAssignmentDTO vendorAssignmentDTO = new VendorAssignmentDTO();
        vendorAssignmentDTO.setOffboardingVendor(true);
        vendorAssignmentDTO.setContractID(34L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(adminEmployee));

        Contract c = new Contract();
        c.setState(State.ACTIVE);
        c.setId(34L);
        c.setServiceContractOwner(adminEmployee);
        c.setDescription("MNO");

        Employee e = new Employee();
        e.setId(67L);
        e.setName("Test user");
        e.setAccessScopes(new Scope[]{Scope.VENDOR});
        e.setRole("Product Manager");

        Vendor v = new Vendor();
        v.setId(23L);
        v.setFirstName("Test");
        v.setLastName("user");
        v.setRole("Product Manager");
        v.setStartDate(date);
        v.setServiceContract(c);
        v.setEmployee(e);

        when(contractRepository.findById(34L)).thenReturn(Optional.of(c));
        when(vendorRepository.findById(23L)).thenReturn(Optional.of(v));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(v);
        VendorGetDTO ven = service.updateVendorAssignment(vendorAssignmentDTO, 23L, 1L);
        assertNotNull(ven);

    }

    @Test
    void testUnAssignContractToVendor_INCORRECT_MAPPING() {
        service.vendorMapper = mapper;
        Employee adminEmployee = new Employee();
        adminEmployee.setId(1L);
        adminEmployee.setRole("Administrator");
        adminEmployee.setAccessScopes(new Employee.Scope[]{Employee.Scope.ADMIN});

        int date = (int)Instant.now().getEpochSecond();
        VendorAssignmentDTO vendorAssignmentDTO = new VendorAssignmentDTO();
        vendorAssignmentDTO.setContractID(29L);
        vendorAssignmentDTO.setOffboardingVendor(true);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(adminEmployee));

        Contract c = new Contract();
        c.setState(State.ACTIVE);
        c.setId(34L);
        c.setServiceContractOwner(adminEmployee);
        c.setDescription("MNO");

        Employee e = new Employee();
        e.setId(67L);
        e.setName("Test user");
        e.setAccessScopes(new Scope[]{Scope.VENDOR});
        e.setRole("Product Manager");

        Vendor v = new Vendor();
        v.setId(23L);
        v.setFirstName("Test");
        v.setLastName("user");
        v.setRole("Product Manager");
        v.setStartDate(date);
        v.setServiceContract(c);
        v.setEmployee(e);

        when(contractRepository.findById(34L)).thenReturn(Optional.of(c));
        when(vendorRepository.findById(23L)).thenReturn(Optional.of(v));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(v);
        assertThrows(RequestException.class, () -> service.updateVendorAssignment(vendorAssignmentDTO, 23L, 1L));
    }

    @Test
    void testUnAssignContractToVendor_UNAUTHORIZED() {
        service.vendorMapper = mapper;
        Employee adminEmployee = new Employee();
        adminEmployee.setId(1L);
        adminEmployee.setRole("Administrator");
        adminEmployee.setAccessScopes(new Employee.Scope[]{Employee.Scope.ADMIN});

        Employee secondUser = new Employee();
        secondUser.setId(33L);
        secondUser.setRole("Service Owner");
        secondUser.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        int date = (int)Instant.now().getEpochSecond();
        VendorAssignmentDTO vendorAssignmentDTO = new VendorAssignmentDTO();
        vendorAssignmentDTO.setOffboardingVendor(true);
        vendorAssignmentDTO.setContractID(34L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(adminEmployee));
        when(employeeRepository.findById(33L)).thenReturn(Optional.of(secondUser));

        Contract c = new Contract();
        c.setState(State.ACTIVE);
        c.setId(34L);
        c.setServiceContractOwner(adminEmployee);
        c.setDescription("MNO");

        Employee e = new Employee();
        e.setId(67L);
        e.setName("Test user");
        e.setAccessScopes(new Scope[]{Scope.VENDOR});
        e.setRole("Product Manager");

        Vendor v = new Vendor();
        v.setId(23L);
        v.setFirstName("Test");
        v.setLastName("user");
        v.setRole("Product Manager");
        v.setStartDate(date);
        v.setServiceContract(c);
        v.setEmployee(e);

        when(contractRepository.findById(34L)).thenReturn(Optional.of(c));
        when(vendorRepository.findById(23L)).thenReturn(Optional.of(v));
        when(vendorRepository.save(any(Vendor.class))).thenReturn(v);
        assertThrows(UnauthorizedException.class, () -> service.updateVendorAssignment(vendorAssignmentDTO, 23L, 33L));
    }

    @Test
    void testGetVendorByID() {
        service.vendorMapper = mapper;
        Long vendorId = 1L;
        Vendor vendor = new Vendor();
        vendor.setId(vendorId);
        vendor.setFirstName("Manish");
        vendor.setLastName("Jindal");
        vendor.setEmployee(new Employee("Manish", "Admin"));
        vendor.getEmployee().setId(23L);
        when(vendorRepository.findById(vendorId)).thenReturn(Optional.of(vendor));
        VendorGetDTO result = service.getVendorByID(vendorId);
        assertNotNull(result);
    }

    @Test
    void testGetVendorByEmployeeID() {
        service.vendorMapper = mapper;
        Long vendorId = 1L;
        Vendor vendor = new Vendor();
        vendor.setId(vendorId);
        vendor.setFirstName("Manish");
        vendor.setLastName("Jindal");
        vendor.setEmployee(new Employee("Manish", "Admin"));
        vendor.getEmployee().setId(23L);
        when(vendorRepository.findByEmployeeID(23L)).thenReturn(Arrays.asList(new Vendor[]{vendor}));
        List<VendorGetDTO> result = service.getVendorByEmployeeID(23L);
        assertNotNull(result);
    }

    @Test
    void testGetAllVendors() {
        service.vendorMapper = mapper;
        Long vendorId = 1L;
        Vendor vendor = new Vendor();
        vendor.setId(vendorId);
        vendor.setFirstName("Manish");
        vendor.setLastName("Jindal");
        vendor.setEmployee(new Employee("Manish", "Admin"));
        vendor.getEmployee().setId(23L);
        when(vendorRepository.findAll()).thenReturn(Arrays.asList(new Vendor[]{vendor}));
        List<VendorGetDTO> result = service.getAllVendors();
        assertNotNull(result);
    }

    @Test
    void testGetUnassignedVendors() {
        service.vendorMapper = mapper;
        Long vendorId = 1L;
        Vendor vendor = new Vendor();
        vendor.setId(vendorId);
        vendor.setFirstName("Manish");
        vendor.setLastName("Jindal");
        vendor.setEmployee(new Employee("Manish", "Admin"));
        vendor.getEmployee().setId(23L);
        when(vendorRepository.findUnassignedVendors()).thenReturn(Arrays.asList(new Vendor[]{vendor}));
        List<VendorGetDTO> result = service.getUnassignedVendors();
        assertNotNull(result);
    }

    @Test
    void testCreateVendor_BAD_REQUEST() {
        assertThrows(RequestException.class, () -> service.createVendor(new VendorUpsertDTO(), 1L));
    }

    @Test
    void testCreateVendor_BAD_REQUEST_Lastname_missing() {
        VendorUpsertDTO v2 = new VendorUpsertDTO("Manish", "", "Role", 1234567l);
        assertThrows(RequestException.class, () -> service.createVendor(v2, 1L));
    }
    @Test
    void testCreateVendor_BAD_REQUEST_Role_missing() {
        VendorUpsertDTO v2 = new VendorUpsertDTO("Manish", "Jindal", "", 1234567l);
        assertThrows(RequestException.class, () -> service.createVendor(v2, 1L));
    }
}
