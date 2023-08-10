package com.intuit.cms.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.intuit.cms.models.dto.VendorAssignmentDTO;
import com.intuit.cms.models.dto.VendorGetDTO;
import com.intuit.cms.models.dto.VendorUpsertDTO;
import com.intuit.cms.services.VendorsService;

public class VendorsControllerTest {

    @Mock
    private VendorsService service;

    @InjectMocks
    private VendorsController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateVendor() {
        VendorGetDTO vendor = new VendorGetDTO();
        VendorUpsertDTO vendorUpsert = new VendorUpsertDTO();
        when(service.createVendor(vendorUpsert, 1l)).thenReturn(vendor);
        VendorGetDTO result = controller.createVendor(vendorUpsert, 1l);
        assertEquals(result, vendor);
    }

    @Test
    void testGetUnassignedVendors() {
        VendorGetDTO vendor = new VendorGetDTO();
        List<VendorGetDTO> vendors = new ArrayList<>();
        vendors.add(vendor);
        when(service.getUnassignedVendors()).thenReturn(vendors);
        List<VendorGetDTO> result = controller.getUnassignedVendors();
        assertEquals(result, vendors);
    }

    @Test
    void testGetVendorByEmployeeID() {
        VendorGetDTO vendor = new VendorGetDTO();
        List<VendorGetDTO> vendors = new ArrayList<>();
        vendors.add(vendor);
        when(service.getVendorByEmployeeID(1L)).thenReturn(vendors);
        List<VendorGetDTO> result = controller.getVendors(Optional.of(1L));
        assertEquals(result, vendors);
    }

    @Test
    void testGetVendorByID() {
        VendorGetDTO vendor = new VendorGetDTO();
        when(service.getVendorByID(1l)).thenReturn(vendor);
        VendorGetDTO result = controller.getVendorByID(1l);
        assertEquals(result, vendor);
    }

    @Test
    void testGetAllVendors() {
        VendorGetDTO vendor = new VendorGetDTO();
        List<VendorGetDTO> vendors = new ArrayList<>();
        vendors.add(vendor);
        when(service.getAllVendors()).thenReturn(vendors);
        List<VendorGetDTO> result = controller.getVendors(Optional.empty());
        assertEquals(result, vendors);
    }

    @Test
    void testUpdateVendorAssignment() {
        VendorGetDTO vendor = new VendorGetDTO();
        VendorAssignmentDTO vendorUpsert = new VendorAssignmentDTO();
        when(service.updateVendorAssignment(vendorUpsert, 1l, 2l)).thenReturn(vendor);
        VendorGetDTO result = controller.updateVendorAssignment(vendorUpsert, 1l, 2l);
        assertEquals(result, vendor);
    }
}
