package com.intuit.cms.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.intuit.cms.models.dto.ContractGetDTO;
import com.intuit.cms.models.dto.ContractUpsertDTO;
import com.intuit.cms.services.ContractsService;

import com.intuit.cms.constants.HttpHeaders;

@RestController
public class ContractsController {

    @Autowired
    ContractsService contractsService;

    @PostMapping("/contracts/")
    public ContractGetDTO createContract(
            @RequestBody ContractUpsertDTO newContract,
            @RequestHeader(HttpHeaders.USER_ID) Long userID
        ) {
        return contractsService.createContract(newContract, userID);
    }

    @PutMapping("/contracts/{id}")
    public ContractGetDTO updateContract(
            @RequestBody ContractUpsertDTO updateContract,
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.USER_ID) Long userID
        ) {
        return contractsService.updateContract(updateContract, id, userID);
    }

    @GetMapping("/contracts/{id}")
    public ContractGetDTO getContractbyID(@PathVariable Long id) {
        return contractsService.getContractByID(id);
    }

    @GetMapping("/contracts")
    public List<ContractGetDTO> getContracts(@RequestParam(name = "ownerId") Optional<Long> ownerId) {
        if (ownerId.isPresent()) {
            return contractsService.getContractsByOwnerID(ownerId.get());
        }
        return contractsService.getContracts();
    }
}
