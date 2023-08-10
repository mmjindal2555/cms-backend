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

import com.intuit.cms.models.dto.VendorAssignmentDTO;
import com.intuit.cms.models.dto.VendorGetDTO;
import com.intuit.cms.models.dto.VendorUpsertDTO;

import com.intuit.cms.constants.HttpHeaders;

import com.intuit.cms.services.VendorsService;

@RestController
public class VendorsController {

    @Autowired
    VendorsService service;


    @PostMapping("/vendors/")
    public VendorGetDTO createVendor(
            @RequestBody VendorUpsertDTO newVendor,
            @RequestHeader(HttpHeaders.USER_ID) Long userID
        ) {
        return service.createVendor(newVendor, userID);
    }

    @PutMapping("/vendors/{id}/assignment")
    public VendorGetDTO updateVendorAssignment(
            @RequestBody VendorAssignmentDTO updateVendor,
            @PathVariable Long id,
            @RequestHeader(HttpHeaders.USER_ID) Long userID
        ) {
            return service.updateVendorAssignment(updateVendor, id, userID);

    }

    @GetMapping("/vendors/{id}")
    public VendorGetDTO getVendorByID(@PathVariable Long id){
        return service.getVendorByID(id);
    }

    @GetMapping("/vendors")
    public List<VendorGetDTO> getVendors(@RequestParam(name = "employeeID") Optional<Long> empID){
        if(empID.isPresent())
            return service.getVendorByEmployeeID(empID.get());
        return service.getAllVendors();
    }

    @GetMapping("/vendors/unassigned")
    public List<VendorGetDTO> getUnassignedVendors(){
        return service.getUnassignedVendors();
    }




}
