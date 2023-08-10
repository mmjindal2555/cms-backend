package com.intuit.cms.services;

import java.util.List;

import com.intuit.cms.models.dto.VendorAssignmentDTO;
import com.intuit.cms.models.dto.VendorGetDTO;
import com.intuit.cms.models.dto.VendorUpsertDTO;

public interface VendorsService {
    public VendorGetDTO createVendor(VendorUpsertDTO newVendor, Long userID);
    public VendorGetDTO updateVendorAssignment(VendorAssignmentDTO updateVendor, Long id, Long userID);
    public VendorGetDTO getVendorByID(Long id);
    public List<VendorGetDTO> getVendorByEmployeeID(Long empID);
    public List<VendorGetDTO> getAllVendors();
    public List<VendorGetDTO> getUnassignedVendors();
}
