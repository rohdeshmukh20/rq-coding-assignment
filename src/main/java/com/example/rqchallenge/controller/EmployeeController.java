package com.example.rqchallenge.controller;


import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.dto.request.CreateEmployeeRequest;
import com.example.rqchallenge.employees.IEmployeeController;
import com.example.rqchallenge.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
public class EmployeeController implements IEmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
        try {
            return employeeService.getEmployees();
        } catch (Exception exception) {
            log.error("Get all employees operation failed {}",exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        ResponseEntity<List<Employee>> entity;
        try {
            if (!StringUtils.isEmpty(searchString)) {
                 return employeeService.getEmployeesByName(searchString);
            } else {
                log.error("search string cannot be empty");
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception exception) {
            log.error("Search operation failed {}",exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        ResponseEntity<List<Employee>> entity;
        try {
            if (!StringUtils.isEmpty(id)) {
                return employeeService.getEmployeeById(id);
            } else {
                log.error("id cannot be empty");
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception exception) {
            log.error("Error while searching employee from ID {} : {}",
                    id, exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        try {
            ResponseEntity<Integer> responseEntity = employeeService.getHighestSalaryOfEmployees();
            return responseEntity;
        } catch (Exception exception) {
            log.error("Error while processing highest salary of employee");
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        try {
            return employeeService.getTopTenHighestEarningEmployeeNames();
        } catch (Exception exception) {
            log.error("Error while processing top 10 highest salary of employee");
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<Employee> createEmployee(@RequestBody CreateEmployeeRequest createEmployeeRequest) {
        try {
            if (validateCreateEmployeeRequest(createEmployeeRequest)) {
                return employeeService.createEmployee(createEmployeeRequest);
            } else {
                log.error("Request body is invalid");
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception exception) {
            log.error("Error while creating employee with name {}: {}",createEmployeeRequest.getName(),
                    exception.toString());
            return ResponseEntity.internalServerError().build();
        }
    }

    private boolean validateCreateEmployeeRequest(CreateEmployeeRequest createEmployeeRequest) {
        try {
            if (Objects.isNull(createEmployeeRequest) || StringUtils.isEmpty(createEmployeeRequest.getAge().toString()) ||
                    StringUtils.isEmpty(createEmployeeRequest.getName().toString()) ||
                    StringUtils.isEmpty(createEmployeeRequest.getSalary().toString())) {
                return false;
            }
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        ResponseEntity<List<Employee>> entity;
        try {
            if (!StringUtils.isEmpty(id)) {
                return employeeService.deleteEmployeeById(id);
            } else {
                log.error("id cannot be empty");
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception exception) {
            log.error("Error while deleteing employee for ID {} : {}",
                    id, exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
