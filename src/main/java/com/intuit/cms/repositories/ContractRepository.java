package com.intuit.cms.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.intuit.cms.models.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    @Query(value = "SELECT c FROM Contract c WHERE c.serviceContractOwner.id = :id")
    List<Contract> findContractsByOwnerID(@Param("id") Long id);

}
