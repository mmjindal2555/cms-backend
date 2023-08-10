package com.intuit.cms.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class VendorMapperTest {
    @Test
    void testFormatEpochToDate() {
        long epoch = 1690396648;
        String date = VendorMapper.formatEpochToDate(epoch);
        assertEquals("26th JULY 2023", date);
    }
    @Test
    void testFormatEpochToDate2() {
        long epoch = 1690848000;
        String date = VendorMapper.formatEpochToDate(epoch);
        assertEquals("1st AUGUST 2023", date);
    }
    @Test
    void testFormatEpochToDate3() {
        long epoch = 1690848000+90061;
        String date = VendorMapper.formatEpochToDate(epoch);
        assertEquals("2nd AUGUST 2023", date);
    }
    @Test
    void testFormatEpochToDate4() {
        long epoch = 1690848000+90061+90061;
        String date = VendorMapper.formatEpochToDate(epoch);
        assertEquals("3rd AUGUST 2023", date);
    }
    @Test
    void testFormatEpochToDate5() {
        long epoch = 1690848000+(90061*10);
        String date = VendorMapper.formatEpochToDate(epoch);
        assertEquals("11th AUGUST 2023", date);
    }
}
