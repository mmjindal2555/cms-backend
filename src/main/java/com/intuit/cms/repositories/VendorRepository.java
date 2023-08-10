package com.intuit.cms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.intuit.cms.models.Vendor;


public interface VendorRepository extends JpaRepository<Vendor, Long> {

    @Query(value = "SELECT u FROM Vendor u WHERE u.serviceContract = NULL")
    List<Vendor> findUnassignedVendors();

    @Query(value = "SELECT u FROM Vendor u WHERE u.employee.id = :id")
    List<Vendor> findByEmployeeID(@Param("id") Long id);

}
