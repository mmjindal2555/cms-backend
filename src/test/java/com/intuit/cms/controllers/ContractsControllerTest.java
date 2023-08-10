package com.intuit.cms.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ResponseStatusException;

import com.intuit.cms.models.dto.ContractGetDTO;
import com.intuit.cms.models.dto.ContractUpsertDTO;
import com.intuit.cms.services.ContractsService;

public class ContractsControllerTest {

    @Mock
    private ContractsService contractsService;

    @InjectMocks
    private ContractsController contractsController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateContract() {
        ContractUpsertDTO newContract = new ContractUpsertDTO();
        Long userId = 123L;
        ContractGetDTO contractGetDTO = new ContractGetDTO();
        when(contractsService.createContract(newContract, userId)).thenReturn(contractGetDTO);
        ContractGetDTO result = contractsController.createContract(newContract, userId);
        assertNotNull(result);
    }

    @Test
    public void testUpdateContract() {
        Long contractId = 456L;
        ContractUpsertDTO updateContract = new ContractUpsertDTO();
        Long userId = 123L;
        ContractGetDTO contractGetDTO = new ContractGetDTO();
        when(contractsService.updateContract(updateContract, contractId, userId)).thenReturn(contractGetDTO);
        ContractGetDTO result = contractsController.updateContract(updateContract, contractId, userId);
        assertNotNull(result);
        assertEquals(result, contractGetDTO);
    }

    @Test
    public void testGetContractById() {
        Long contractId = 456L;
        ContractGetDTO contractGetDTO = new ContractGetDTO();
        when(contractsService.getContractByID(contractId)).thenReturn(contractGetDTO);
        ContractGetDTO result = contractsController.getContractbyID(contractId);
        assertNotNull(result);
        assertEquals(result, contractGetDTO);
    }

    @Test
    public void testGetContractsByOwnerId() {
        Long ownerId = 789L;
        List<ContractGetDTO> contracts = new ArrayList<>();
        when(contractsService.getContractsByOwnerID(ownerId)).thenReturn(contracts);
        List<ContractGetDTO> result = contractsController.getContracts(Optional.of(ownerId));
        assertNotNull(result);
    }

    @Test
    public void testGetAllContracts() {
        List<ContractGetDTO> contracts = new ArrayList<>();
        when(contractsService.getContracts()).thenReturn(contracts);
        List<ContractGetDTO> result = contractsController.getContracts(Optional.empty());
        assertNotNull(result);
    }

}
