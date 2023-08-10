package com.intuit.cms.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class VendorTest {
    @Test
    void testGetFullName() {
        Vendor v = new Vendor();
        v.setFirstName("Manish");
        v.setLastName("Jindal");
        assertEquals(v.getFullName(), "Manish Jindal");
    }

    @Test
    void testGetFullNameWithID() {
        Vendor v = new Vendor();
        v.setFirstName("Manish");
        v.setLastName("Jindal");
        v.setId(1L);
        assertEquals(v.getFullNameWithID(), "Manish Jindal (1)");
    }
}
