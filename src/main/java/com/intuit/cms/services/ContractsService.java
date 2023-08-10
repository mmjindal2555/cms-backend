package com.intuit.cms.services;

import java.util.List;

import com.intuit.cms.models.dto.ContractGetDTO;
import com.intuit.cms.models.dto.ContractUpsertDTO;

public interface ContractsService {
    public ContractGetDTO createContract(ContractUpsertDTO newContract, Long userID);
    public ContractGetDTO updateContract(ContractUpsertDTO newContract, Long id, Long userID);
    public ContractGetDTO getContractByID(Long id);
    public List<ContractGetDTO> getContractsByOwnerID(Long ownerId);
    public List<ContractGetDTO> getContracts();
}
