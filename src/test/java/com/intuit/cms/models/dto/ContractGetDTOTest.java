package com.intuit.cms.models.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.*;

import org.junit.jupiter.api.Test;

public class ContractGetDTOTest {

    @Test
    void testEquals() {
        ContractGetDTO one = new ContractGetDTO();
        ContractGetDTO two = new ContractGetDTO();
        assertEquals(one, two);
        two.setDescription("12");
        assertNotEquals(one, two);
        one.setDescription("12");
        two.setCurrentState("st");
        assertNotEquals(one, two);
        one.setCurrentState("st");
        two.setId(123l);
        assertNotEquals(one, two);
        two.setOwner("qwer");
        one.setId(123l);
        assertNotEquals(one, two);

        one.setOwner("qwer");
        one.setAssignedVendors(new ArrayList<String>());
        assertNotEquals(one, two);

        assertNotEquals(one, new VendorGetDTO());
    }
}
