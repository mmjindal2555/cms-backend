package com.intuit.cms.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.intuit.cms.controllerAdvice.RequestException;
import com.intuit.cms.controllerAdvice.ResourceNotFoundException;
import com.intuit.cms.controllerAdvice.UnauthorizedException;
import com.intuit.cms.mappers.ContractMapper;
import com.intuit.cms.models.Contract;
import com.intuit.cms.models.Employee;
import com.intuit.cms.models.Vendor;
import com.intuit.cms.models.Contract.State;
import com.intuit.cms.models.Employee.Scope;
import com.intuit.cms.models.dto.ContractGetDTO;
import com.intuit.cms.models.dto.ContractUpsertDTO;
import com.intuit.cms.repositories.ContractRepository;
import com.intuit.cms.repositories.EmployeeRepository;
import com.intuit.cms.repositories.VendorRepository;

@SpringJUnitConfig
@SpringBootTest
@ActiveProfiles("test")
public class ContractsServiceImplTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private VendorRepository vendorRepository;

    @Autowired
    ContractMapper contractMapper;

    @InjectMocks
    private ContractsServiceImpl contractService;

    @BeforeEach
    public void setUp() {
        contractService.contractMapper = contractMapper;
    }

    @Test
    void testCreateContractAdmin() {

        Employee adminEmployee = new Employee();
        adminEmployee.setId(1L);
        adminEmployee.setAccessScopes(new Employee.Scope[]{Employee.Scope.ADMIN});
        ContractUpsertDTO contractDto = ContractUpsertDTO.builder()
            .description("Test contract")
            .build();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(adminEmployee));
        Contract c = new Contract();
        c.setServiceContractOwner(adminEmployee);
        c.setState(State.DRAFT);
        when(contractRepository.save(any(Contract.class))).thenReturn(c);
        ContractGetDTO result = contractService.createContract(contractDto, 1L);
        assertNotNull(result);
    }

    @Test
    void testCreateContractServiceOwner() {

        Employee serviceOwnerEmployee = new Employee();
        serviceOwnerEmployee.setId(2L);
        serviceOwnerEmployee.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        ContractUpsertDTO contractDto = new ContractUpsertDTO();
        contractDto.setDescription("Test contract");
        Contract c = new Contract();
        c.setServiceContractOwner(serviceOwnerEmployee);
        c.setState(State.DRAFT);
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(serviceOwnerEmployee));
        when(contractRepository.save(any(Contract.class))).thenReturn(c);
        ContractGetDTO result = contractService.createContract(contractDto, 2L);
        assertNotNull(result);
    }

    @Test
    void testCreateContractUnauthorized() {

        Employee regularEmployee = new Employee();
        regularEmployee.setId(3L);
        regularEmployee.setAccessScopes(new Employee.Scope[]{Employee.Scope.VENDOR});
        ContractUpsertDTO contractDto = new ContractUpsertDTO();
        contractDto.setDescription("Test contract");
        when(employeeRepository.findById(3L)).thenReturn(Optional.of(regularEmployee));
        assertThrows(UnauthorizedException.class, () -> contractService.createContract(contractDto, 3L));
    }

    @Test
    void testCreateContractEmployeeNotFound() {
        when(employeeRepository.findById(4L)).thenReturn(Optional.empty());
        Contract c = new Contract("desc", new Employee("Emp", "role", new Scope[]{}, 1L));
        when(contractRepository.save(any(Contract.class))).thenReturn(c);
        ContractUpsertDTO c2 = new ContractUpsertDTO("desc", State.DRAFT);
        assertThrows(ResourceNotFoundException.class, () -> contractService.createContract(c2, 4L));
    }

    @Test
    void testGetContractByID() {


        Employee serviceOwnerEmployee = new Employee();
        serviceOwnerEmployee.setName("ABC");
        serviceOwnerEmployee.setId(5L);
        serviceOwnerEmployee.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        Contract c = new Contract();
        c.setState(State.DRAFT);
        c.setId(50L);
        c.setServiceContractOwner(serviceOwnerEmployee);
        c.setDescription("MNO");

        when(contractRepository.findById(50L)).thenReturn(Optional.of(c));
        ContractGetDTO result = contractService.getContractByID(50L);
        assertNotNull(result);
        ContractGetDTO expected = ContractGetDTO.builder()
            .owner("ABC")
            .id(50L)
            .currentState("DRAFT")
            .assignedVendors(new ArrayList<String>())
            .description("MNO").build();

        assertNotNull(result);
        assertEquals(result.getDescription(), expected.getDescription());
        assertEquals(result.getId(), expected.getId());
        assertEquals(result.getCurrentState(), expected.getCurrentState());
        assertEquals(result.getOwner(), expected.getOwner());
        assertEquals(result.getAssignedVendors(), expected.getAssignedVendors());
    }

    @Test
    void testGetContractByInvalidId() {
        when(contractRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> contractService.getContractByID(2L));
    }

    @Test
    void testUpdateContractValidUserAndContractOwner() {

        Employee user = new Employee();
        user.setId(2L);
        user.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        Contract originalContract = new Contract();
        originalContract.setId(1L);
        originalContract.setDescription("Original description");
        originalContract.setState(State.DRAFT);
        originalContract.setServiceContractOwner(user);

        ContractUpsertDTO updateContractDto = new ContractUpsertDTO();
        updateContractDto.setDescription("Updated description");
        updateContractDto.setState(State.APPROVED);

        Contract updatedContract = new Contract();
        updatedContract.setId(1L);
        updatedContract.setDescription("Updated description");
        updatedContract.setState(State.APPROVED);

        when(contractRepository.findById(1L)).thenReturn(Optional.of(originalContract));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(user));
        when(contractRepository.save(any(Contract.class))).thenReturn(originalContract);

        ContractGetDTO result = contractService.updateContract(updateContractDto, 1L, 2L);
        assertNotNull(result);
        assertEquals(updateContractDto.getDescription(), result.getDescription());
        assertEquals(updateContractDto.getState().toString(), result.getCurrentState());
    }

    @Test
    void testUpdateContractValidUserAndContractOwner2() {

        Employee user = new Employee();
        user.setId(2L);
        user.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        Contract originalContract = new Contract();
        originalContract.setId(1L);
        originalContract.setDescription("Original description");
        originalContract.setState(State.ACTIVE);
        originalContract.setServiceContractOwner(user);
        HashSet<Vendor> set = new HashSet<>();
        set.add(new Vendor("A", "B", "r", 1234567));
        originalContract.setAssignedVendors(set);

        ContractUpsertDTO updateContractDto = new ContractUpsertDTO();
        updateContractDto.setDescription("Updated description");
        updateContractDto.setState(State.INACTIVE);

        Contract updatedContract = new Contract();
        updatedContract.setId(1L);
        updatedContract.setDescription("Updated description");
        updatedContract.setState(State.INACTIVE);
        updatedContract.setServiceContractOwner(user);
        updatedContract.setAssignedVendors(new HashSet<>());

        when(contractRepository.findById(1L)).thenReturn(Optional.of(originalContract));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(user));
        when(contractRepository.save(any(Contract.class))).thenReturn(updatedContract);

        ContractGetDTO result = contractService.updateContract(updateContractDto, 1L, 2L);
        assertNotNull(result);
        assertEquals(updateContractDto.getDescription(), result.getDescription());
        assertEquals(updateContractDto.getState().toString(), result.getCurrentState());
        assertEquals(result.getAssignedVendors(), new ArrayList<>());
    }

    @Test
    void testUpdateContractValidUserAndContractOwnerBUTIncorrectState() {

        Employee user = new Employee();
        user.setId(2L);
        user.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        Contract originalContract = new Contract();
        originalContract.setId(1L);
        originalContract.setDescription("Original description");
        originalContract.setState(State.DRAFT);
        originalContract.setServiceContractOwner(user);

        ContractUpsertDTO updateContractDto = new ContractUpsertDTO();
        updateContractDto.setDescription("Updated description");
        updateContractDto.setState(State.ACTIVE);

        Contract updatedContract = new Contract();
        updatedContract.setId(1L);
        updatedContract.setDescription("Updated description");
        updatedContract.setState(State.ACTIVE);

        when(contractRepository.findById(1L)).thenReturn(Optional.of(originalContract));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(user));

        assertThrows(RequestException.class, () -> contractService.updateContract(updateContractDto, 1L, 2L));
    }

    @Test
    void testUpdateContractValidUserNoOrgContract() {

        Employee user = new Employee();
        user.setId(2L);
        user.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        ContractUpsertDTO updateContractDto = new ContractUpsertDTO();
        updateContractDto.setDescription("Updated description");
        updateContractDto.setState(State.DRAFT);

        Contract updatedContract = new Contract();
        updatedContract.setId(1L);
        updatedContract.setServiceContractOwner(user);
        updatedContract.setDescription("Updated description");
        updatedContract.setState(State.DRAFT);

        when(contractRepository.findById(1L)).thenReturn(Optional.empty());
        when(contractRepository.save(any(Contract.class))).thenReturn(updatedContract);
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(user));

        ContractGetDTO result = contractService.updateContract(updateContractDto, 1L, 2L);
        assertNotNull(result);
        assertEquals(updateContractDto.getDescription(), result.getDescription());
        assertEquals(updateContractDto.getState().toString(), result.getCurrentState());
        assertEquals(result.getAssignedVendors(), new ArrayList<>());
    }

    @Test
    void testUpdateContractValidUserNoOrgContractWithoutDescription() {

        Employee user = new Employee();
        user.setId(2L);
        user.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        ContractUpsertDTO updateContractDto = new ContractUpsertDTO();
        updateContractDto.setState(State.DRAFT);

        Contract updatedContract = new Contract();
        updatedContract.setId(1L);
        updatedContract.setServiceContractOwner(user);
        updatedContract.setDescription("Updated description");
        updatedContract.setState(State.DRAFT);

        when(contractRepository.findById(1L)).thenReturn(Optional.empty());
        when(contractRepository.save(any(Contract.class))).thenReturn(updatedContract);
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(user));

        assertThrows(ResourceNotFoundException.class, () -> contractService.updateContract(updateContractDto, 1L, 2L));
    }

    @Test
    void testUpdateContractUnauthorized2() {

        Employee user = new Employee();
        user.setId(2L);
        user.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});
        Employee user2 = new Employee();
        user2.setId(23L);
        user2.setAccessScopes(new Employee.Scope[]{Employee.Scope.VENDOR});

        Contract originalContract = new Contract();
        originalContract.setId(1L);
        originalContract.setDescription("Original description");
        originalContract.setState(State.DRAFT);
        originalContract.setServiceContractOwner(user);

        ContractUpsertDTO updateContractDto = new ContractUpsertDTO();
        updateContractDto.setDescription("Updated description");
        updateContractDto.setState(State.ACTIVE);

        when(contractRepository.findById(1L)).thenReturn(Optional.of(originalContract));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(user));
        when(employeeRepository.findById(23L)).thenReturn(Optional.of(user2));

        assertThrows(UnauthorizedException.class, () -> contractService.updateContract(updateContractDto, 1L, 23L));
    }

    @Test
    void testUpdateContractValidUserAndContractOwnerBUTIncorrectState2() {

        Employee user = new Employee();
        user.setId(2L);
        user.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        Contract originalContract = new Contract();
        originalContract.setId(1L);
        originalContract.setDescription("Original description");
        originalContract.setState(State.INACTIVE);
        originalContract.setServiceContractOwner(user);

        ContractUpsertDTO updateContractDto = new ContractUpsertDTO();
        updateContractDto.setDescription("Updated description");
        updateContractDto.setState(State.ACTIVE);

        Contract updatedContract = new Contract();
        updatedContract.setId(1L);
        updatedContract.setDescription("Updated description");
        updatedContract.setState(State.ACTIVE);

        when(contractRepository.findById(1L)).thenReturn(Optional.of(originalContract));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(user));

        assertThrows(RequestException.class, () -> contractService.updateContract(updateContractDto, 1L, 2L));
    }

    @Test
    void testUpdateContractValidUserAndContractOwnerBUTIncorrectState3() {

        Employee user = new Employee();
        user.setId(2L);
        user.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        Contract originalContract = new Contract();
        originalContract.setId(1L);
        originalContract.setDescription("Original description");
        originalContract.setState(State.APPROVED);
        originalContract.setServiceContractOwner(user);

        ContractUpsertDTO updateContractDto = new ContractUpsertDTO();
        updateContractDto.setDescription("Updated description");
        updateContractDto.setState(State.DRAFT);

        Contract updatedContract = new Contract();
        updatedContract.setId(1L);
        updatedContract.setDescription("Updated description");
        updatedContract.setState(State.DRAFT);

        when(contractRepository.findById(1L)).thenReturn(Optional.of(originalContract));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(user));

        assertThrows(RequestException.class, () -> contractService.updateContract(updateContractDto, 1L, 2L));
    }

    @Test
    void testUpdateContractValidUserAndContractOwnerBUTIncorrectState4() {

        Employee user = new Employee();
        user.setId(2L);
        user.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        Contract originalContract = new Contract();
        originalContract.setId(1L);
        originalContract.setDescription("Original description");
        originalContract.setState(State.APPROVED);
        originalContract.setServiceContractOwner(user);

        ContractUpsertDTO updateContractDto = new ContractUpsertDTO();
        updateContractDto.setDescription("Updated description");
        updateContractDto.setState(State.INACTIVE);

        Contract updatedContract = new Contract();
        updatedContract.setId(1L);
        updatedContract.setDescription("Updated description");
        updatedContract.setState(State.INACTIVE);

        when(contractRepository.findById(1L)).thenReturn(Optional.of(originalContract));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(user));

        assertThrows(RequestException.class, () -> contractService.updateContract(updateContractDto, 1L, 2L));
    }

    @Test
    void testUpdateContractValidUserAndContractOwnerBUTIncorrectState5() {

        Employee user = new Employee();
        user.setId(2L);
        user.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        Employee user2 = new Employee();
        user2.setId(3L);
        user2.setAccessScopes(new Employee.Scope[]{Employee.Scope.ADMIN});

        Contract originalContract = new Contract();
        originalContract.setId(1L);
        originalContract.setDescription("Original description");
        originalContract.setState(State.DRAFT);
        originalContract.setServiceContractOwner(user);

        ContractUpsertDTO updateContractDto = new ContractUpsertDTO();
        updateContractDto.setDescription("Updated description");
        updateContractDto.setState(State.ACTIVE);

        Contract updatedContract = new Contract();
        updatedContract.setId(1L);
        updatedContract.setDescription("Updated description");
        updatedContract.setState(State.ACTIVE);

        when(contractRepository.findById(1L)).thenReturn(Optional.of(originalContract));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(user));
        when(employeeRepository.findById(3L)).thenReturn(Optional.of(user2));

        assertThrows(RequestException.class, () -> contractService.updateContract(updateContractDto, 1L, 3L));
    }

    @Test
    void testUpdateContractValidUserAndContractOwnerBUTIncorrectState6() {

        Employee user = new Employee();
        user.setId(2L);
        user.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        Contract originalContract = new Contract();
        originalContract.setId(1L);
        originalContract.setDescription("Original description");
        originalContract.setState(State.ACTIVE);
        originalContract.setServiceContractOwner(user);

        ContractUpsertDTO updateContractDto = new ContractUpsertDTO();
        updateContractDto.setDescription("Updated description");
        updateContractDto.setState(State.APPROVED);

        Contract updatedContract = new Contract();
        updatedContract.setId(1L);
        updatedContract.setDescription("Updated description");
        updatedContract.setState(State.APPROVED);

        when(contractRepository.findById(1L)).thenReturn(Optional.of(originalContract));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(user));

        assertThrows(RequestException.class, () -> contractService.updateContract(updateContractDto, 1L, 2L));
    }


    @Test
    void testUpdateContractValidUserAndContractOwnerButDifferentUser() {

        Employee user = new Employee();
        user.setId(2L);
        user.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        Employee user2 = new Employee();
        user.setId(20L);
        user.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        Contract originalContract = new Contract();
        originalContract.setId(1L);
        originalContract.setDescription("Original description");
        originalContract.setState(State.APPROVED);
        originalContract.setServiceContractOwner(user);

        ContractUpsertDTO updateContractDto = new ContractUpsertDTO();
        updateContractDto.setDescription("Updated description");
        updateContractDto.setState(State.DRAFT);

        Contract updatedContract = new Contract();
        updatedContract.setId(1L);
        updatedContract.setDescription("Updated description");
        updatedContract.setState(State.DRAFT);

        when(contractRepository.findById(1L)).thenReturn(Optional.of(originalContract));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(user));
        when(employeeRepository.findById(20L)).thenReturn(Optional.of(user2));

        assertThrows(RequestException.class, () -> contractService.updateContract(updateContractDto, 1L, 20L));
    }

    @Test
    void testGetContracts() {

        Employee user = new Employee();
        user.setName("ABC");
        user.setId(2L);
        user.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});
        Contract originalContract = new Contract();
        originalContract.setId(1L);
        originalContract.setDescription("Description 1");
        originalContract.setState(State.DRAFT);
        originalContract.setServiceContractOwner(user);
        Contract originalContract2 = new Contract();
        originalContract2.setId(2L);
        originalContract2.setDescription("Description 2");
        originalContract2.setState(State.DRAFT);
        originalContract2.setServiceContractOwner(user);

        List<Contract> c = new ArrayList<>();
        c.add(originalContract);
        c.add(originalContract2);
        when(contractRepository.findAll()).thenReturn(c);

        List<ContractGetDTO> contracts = contractService.getContracts();
        List<ContractGetDTO> expected = new ArrayList<>();
        expected.add(new ContractGetDTO("Description 1", new ArrayList<>(), user.getName(), State.DRAFT.toString(), 1L));
        expected.add(new ContractGetDTO("Description 2", new ArrayList<>(), user.getName(), State.DRAFT.toString(), 2L));

        assertEquals(expected, contracts);
    }

    @Test
    void testGetContractsByOwnerID() {

        Employee user = new Employee();
        user.setName("ABC");
        user.setId(2L);
        user.setAccessScopes(new Employee.Scope[]{Employee.Scope.SERVICE_OWNER});

        Contract originalContract = new Contract();
        originalContract.setId(1L);
        originalContract.setDescription("Description 1");
        originalContract.setState(State.DRAFT);
        originalContract.setServiceContractOwner(user);

        List<Contract> c = new ArrayList<>();
        c.add(originalContract);
        when(contractRepository.findAll()).thenReturn(c);
        when(contractRepository.findContractsByOwnerID(1L)).thenReturn(c);

        List<ContractGetDTO> contracts = contractService.getContractsByOwnerID(1L);
        List<ContractGetDTO> expected = new ArrayList<>();
        expected.add(new ContractGetDTO("Description 1", new ArrayList<>(), user.getName(), State.DRAFT.toString(), 1L));

        assertEquals(expected, contracts);
    }

    @Test
    void testUpdateContract() {

    }

    @Test
    void testCreateContractService_BAD_REQUEST() {
        assertThrows(RequestException.class, () -> contractService.createContract(new ContractUpsertDTO(), 1L));
    }
}
