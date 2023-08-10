package com.intuit.cms.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.intuit.cms.constants.ErrorResponses;
import com.intuit.cms.constants.SystemConstants;
import com.intuit.cms.controllerAdvice.RequestException;
import com.intuit.cms.controllerAdvice.ResourceNotFoundException;
import com.intuit.cms.controllerAdvice.UnauthorizedException;
import com.intuit.cms.mappers.VendorMapper;
import com.intuit.cms.models.Contract;
import com.intuit.cms.models.Employee;
import com.intuit.cms.models.Employee.Scope;
import com.intuit.cms.models.Vendor;
import com.intuit.cms.models.dto.VendorAssignmentDTO;
import com.intuit.cms.models.dto.VendorGetDTO;
import com.intuit.cms.models.dto.VendorUpsertDTO;
import com.intuit.cms.repositories.ContractRepository;
import com.intuit.cms.repositories.EmployeeRepository;
import com.intuit.cms.repositories.VendorRepository;

@Service
public class VendorsServiceImpl implements VendorsService {

    @Autowired
    EmployeeRepository employeeRespository;

    @Autowired
    VendorRepository vendorRepository;

    @Autowired
    ContractRepository contractRepository;

    @Autowired
    VendorMapper vendorMapper;

    @Override
    public VendorGetDTO createVendor(VendorUpsertDTO newVendor, Long userID) {
        return vendorMapper.toDto(createVendorService(newVendor, userID));
    }

    @Override
    public VendorGetDTO updateVendorAssignment(VendorAssignmentDTO updateVendor, Long id, Long userID) {
        Vendor v = vendorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorResponses.VENDOR_404));

        Employee user = employeeRespository.findById(userID)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorResponses.EMPLOYEE_404));

        Contract currentContract = v.getServiceContract();
        if(currentContract != null && currentContract.getServiceContractOwner().getId() != userID &&
            !user.isAdmin()){
            throw new UnauthorizedException(ErrorResponses.UNAUTHORIZED);
        }

        if(updateVendor.isOffboardingVendor()) {

            if(currentContract == null || updateVendor.getContractID() == null || currentContract.getId() != updateVendor.getContractID()){
                throw new RequestException(ErrorResponses.INCORRECT_MAPPING);
            }
            v.setServiceContract(null);

            return vendorMapper.toDto(vendorRepository.save(v));
        }

        Contract newContract = contractRepository.findById(updateVendor.getNewServiceContractID()).orElse(null);

        if(newContract != null && newContract.getServiceContractOwner().getId() != userID &&
            !user.isAdmin()){
            throw new UnauthorizedException(ErrorResponses.UNAUTHORIZED);
        }

        if(newContract == null) {
            throw new ResourceNotFoundException(ErrorResponses.CONTRACT_404);
        }

        if(updateVendor.getContractID() != null && (currentContract == null || currentContract.getId() != updateVendor.getContractID() )){
            throw new RequestException(ErrorResponses.INCORRECT_MAPPING);
        }

        if(newContract.getState() != Contract.State.ACTIVE) {
            throw new RequestException(ErrorResponses.CONTRACT_NOT_ACTIVE);
        }

        v.setServiceContract(newContract);
        return vendorMapper.toDto(vendorRepository.save(v));

    }

    @Override
    public VendorGetDTO getVendorByID(Long id) {
        Vendor c = vendorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ErrorResponses.VENDOR_404));
        return vendorMapper.toDto(c);
    }

    @Override
    public List<VendorGetDTO> getVendorByEmployeeID(Long empID) {
        return vendorRepository.findByEmployeeID(empID).stream().map(vendorMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<VendorGetDTO> getAllVendors() {
        return vendorRepository.findAll().stream().map(vendorMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<VendorGetDTO> getUnassignedVendors() {
        return vendorRepository.findUnassignedVendors().stream().map(vendorMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    private Vendor addVendor(VendorUpsertDTO vendor){
        Vendor v = new Vendor(vendor.getFirstName(), vendor.getLastName(), vendor.getRole(), vendor.getStartDate());
        Employee e = new Employee(vendor.getFullName(), vendor.getRole(), new Scope[]{Scope.VENDOR}, vendor.getStartDate());
        v.setEmployee(e);
        employeeRespository.save(e);
        return vendorRepository.save(v);
    }

    private Vendor createVendorService(VendorUpsertDTO newVendor, Long userID){
        if(newVendor.getFirstName() == null || newVendor.getFirstName().trim().isEmpty()) {
            throw new RequestException(ErrorResponses.INVALID_ARGUMENTS);
        }
        if(newVendor.getLastName() == null || newVendor.getLastName().trim().isEmpty()) {
            throw new RequestException(ErrorResponses.INVALID_ARGUMENTS);
        }
        if(newVendor.getRole() == null || newVendor.getRole().trim().isEmpty()) {
            throw new RequestException(ErrorResponses.INVALID_ARGUMENTS);
        }
        Employee owner = employeeRespository.findById(userID)
            .orElseThrow(() -> new ResourceNotFoundException(ErrorResponses.EMPLOYEE_404));

        if(newVendor.getStartDate() < SystemConstants.ORG_START_TIME)
            throw new RequestException(ErrorResponses.DATE_TOO_OLD);

        if(newVendor.getStartDate() > (System.currentTimeMillis()/1000 + SystemConstants.A_YEARS_TIME))
            throw new RequestException(ErrorResponses.DATE_TOO_FUTURISTIC);

        if(owner.isAdmin()) {
            return addVendor(newVendor);
        } else {
            throw new UnauthorizedException(ErrorResponses.UNAUTHORIZED);
        }
    }
}
