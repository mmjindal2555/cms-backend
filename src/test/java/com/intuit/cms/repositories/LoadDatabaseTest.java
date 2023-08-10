package com.intuit.cms.repositories;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.intuit.cms.models.Employee;
import com.intuit.cms.models.Employee.Scope;

@SpringJUnitConfig
@SpringBootTest
@ActiveProfiles("test")
public class LoadDatabaseTest {

    @Mock
    EmployeeRepository employeeRepository;


    @Test
    void initDatabase_emptyRepository_preloadsData() throws Exception {
        Mockito.when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        LoadDatabase loadDatabase = new LoadDatabase();
        loadDatabase.initDatabase(employeeRepository).run();

        Mockito.verify(employeeRepository, Mockito.times(4)).save(Mockito.any());
    }

    @Test
    void initDatabase_nonEmptyRepository_doesNotPreloadData() throws Exception {
        Mockito.when(employeeRepository.findAll()).thenReturn(Arrays.asList(
            new Employee("John", "Engineer", new Scope[]{Scope.ADMIN}, Instant.now().getEpochSecond())
        ));

        LoadDatabase loadDatabase = new LoadDatabase();
        loadDatabase.initDatabase(employeeRepository).run();

        Mockito.verify(employeeRepository, Mockito.never()).save(Mockito.any());
    }

}
