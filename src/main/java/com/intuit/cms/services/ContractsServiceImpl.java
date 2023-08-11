package com.intuit.cms.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intuit.cms.constants.ErrorResponses;
import com.intuit.cms.controllerAdvice.RequestException;
import com.intuit.cms.controllerAdvice.ResourceNotFoundException;
import com.intuit.cms.controllerAdvice.UnauthorizedException;
import com.intuit.cms.mappers.ContractMapper;
import com.intuit.cms.models.Contract;
import com.intuit.cms.models.Contract.State;
import com.intuit.cms.models.Employee;
import com.intuit.cms.models.Vendor;
import com.intuit.cms.models.dto.ContractGetDTO;
import com.intuit.cms.models.dto.ContractUpsertDTO;
import com.intuit.cms.repositories.ContractRepository;
import com.intuit.cms.repositories.EmployeeRepository;
import com.intuit.cms.repositories.VendorRepository;

@Service
public class ContractsServiceImpl implements ContractsService {

    @Autowired
    ContractRepository contractRespository;

    @Autowired
    EmployeeRepository employeeRespository;

    @Autowired
    VendorRepository vendorRepository;

    @Autowired
    ContractMapper contractMapper;

    @Override
    public ContractGetDTO createContract(ContractUpsertDTO newContract, Long userID) {
        return contractMapper.toDto(createContractService(newContract, userID));
    }

    @Override
    public ContractGetDTO updateContract(ContractUpsertDTO updateContract, Long id, Long userID) {
        Optional<Contract> originalContractOptional = contractRespository.findById(id);
        if (originalContractOptional.isPresent()) {
            Contract oContract = originalContractOptional.get();
            Employee user = validateContractModificationAuth(oContract, userID);

            if (updateContract.getDescription() != null)
                oContract.setDescription(updateContract.getDescription());
            if (updateContract.getState() != null) {
                validateStateChanges(oContract.getState(), updateContract.getState(), user);
                if (updateContract.getState() == State.INACTIVE) {
                    return contractMapper.toDto(inactivateContract(oContract));
                }
                oContract.setState(updateContract.getState());
            }
            return contractMapper.toDto(contractRespository.save(oContract));
        } else {
            if (updateContract.getDescription() != null) {
                Contract c = createContractService(updateContract, userID);
                return contractMapper.toDto(c);
            }
            throw new RequestException(ErrorResponses.INVALID_ARGUMENTS);
        }
    }

    @Override
    public ContractGetDTO getContractByID(Long id) {
        Contract c = contractRespository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorResponses.CONTRACT_404));
        return contractMapper.toDto(c);
    }

    @Override
    public List<ContractGetDTO> getContractsByOwnerID(Long ownerId) {
        return contractRespository.findContractsByOwnerID(ownerId).stream().map(contractMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContractGetDTO> getContracts() {
        return contractRespository.findAll().stream().map(contractMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    private Contract inactivateContract(Contract c) {
        Vendor[] vendors = c.getAssignedVendors().toArray(new Vendor[c.getAssignedVendors().size()]);
        for (Vendor v : vendors) {
            v.setServiceContract(null);
            vendorRepository.save(v);
        }
        c.setState(State.INACTIVE);
        return contractRespository.save(c);
    }

    private Contract createContractService(ContractUpsertDTO newContract, Long userID) {
        if (newContract.getDescription() == null || newContract.getDescription().trim().isEmpty()) {
            throw new RequestException(ErrorResponses.INVALID_ARGUMENTS);
        }
        Employee owner = validateContractCreationAuth(userID);
        return contractRespository.save(new Contract(newContract.getDescription(), owner));
    }

    private void validateStateChanges(Contract.State current, Contract.State desired, Employee user) {
        if (current == desired)
            return;
        if (current != State.DRAFT && desired == State.DRAFT) {
            throw new RequestException(ErrorResponses.CONTRACT_CANT_BE_MADE_TO_DRAFT);
        }
        if (current != State.ACTIVE && desired == State.INACTIVE) {
            throw new RequestException(ErrorResponses.CONTRACT_NOT_ACTIVE);
        }
        if (current != State.APPROVED && desired == State.ACTIVE) {
            throw new RequestException(ErrorResponses.CONTRACT_NOT_APPROVED);
        }
        if (current != State.DRAFT && desired == State.APPROVED) {
            throw new RequestException(ErrorResponses.CONTRACT_NOT_DRAFT);
        }
        if (desired == State.APPROVED && !user.isAdmin())
            throw new UnauthorizedException(ErrorResponses.UNAUTHORIZED);
    }

    private Employee validateContractCreationAuth(Long userID) {
        Employee owner = employeeRespository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorResponses.EMPLOYEE_404));
        if (!(owner.isAdmin() || owner.isServiceOwner()))
            throw new UnauthorizedException(ErrorResponses.UNAUTHORIZED);
        return owner;
    }

    private Employee validateContractModificationAuth(Contract contract, Long userID) {
        Employee user = employeeRespository.findById(userID)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorResponses.EMPLOYEE_404));
        if (!contract.getServiceContractOwner().getId().equals(userID) && !user.isAdmin())
            throw new UnauthorizedException(ErrorResponses.UNAUTHORIZED);
        return user;
    }
}
