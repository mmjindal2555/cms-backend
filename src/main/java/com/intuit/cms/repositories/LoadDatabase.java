package com.intuit.cms.repositories;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.intuit.cms.models.Employee;
import com.intuit.cms.models.Employee.Scope;

@Configuration
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(EmployeeRepository repository) {

    if (repository.findAll().isEmpty()) {
      return args -> {
        log.info("Preloading " +
            repository.save(
                new Employee(
                    "Alex",
                    "Administrator",
                    new Scope[] { Scope.ADMIN },
                    Instant.now().minusSeconds(60 * 60 * 24 * 14).getEpochSecond())));
        log.info(
            "Preloading " +
                repository.save(
                    new Employee(
                        "Bob",
                        "Product Manager",
                        new Scope[] { Scope.SERVICE_OWNER },
                        Instant.now().minusSeconds(60 * 60 * 24 * 7).getEpochSecond())));
        log.info(
            "Preloading " +
                repository.save(
                    new Employee(
                        "Christine",
                        "Product Manager",
                        new Scope[] { Scope.SERVICE_OWNER },
                        Instant.now().minusSeconds(60 * 60 * 24 * 6).getEpochSecond())));
        log.info(
            "Preloading " +
                repository.save(
                    new Employee(
                        "Danny",
                        "Product Manager",
                        new Scope[] { Scope.SERVICE_OWNER },
                        Instant.now().minusSeconds(60 * 60 * 24 * 2).getEpochSecond())));
      };
    }
    return args -> {};
  }
}
