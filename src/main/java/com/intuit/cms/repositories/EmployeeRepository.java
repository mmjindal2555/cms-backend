package com.intuit.cms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.intuit.cms.models.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
