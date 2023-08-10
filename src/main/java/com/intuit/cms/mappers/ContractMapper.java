package com.intuit.cms.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.intuit.cms.models.Contract;
import com.intuit.cms.models.Vendor;
import com.intuit.cms.models.dto.ContractGetDTO;

@Component
public class ContractMapper {
    public ContractGetDTO toDto(Contract contract) {
        String description = contract.getDescription();
        List<String> vendors = contract
            .getAssignedVendors()
            .stream()
            .map(Vendor::getFullNameWithID)
            .collect(Collectors.toList());
        String owner = contract.getServiceContractOwner().getName();
        String state = contract.getState().toString();
        return new ContractGetDTO(description, vendors, owner, state, contract.getId());
    }
}
