package com.example.rqchallenge;

import com.example.rqchallenge.controller.EmployeeController;
import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.dto.request.CreateEmployeeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
@SpringBootTest
@AutoConfigureMockMvc
class RqChallengeApplicationTests {

    @Autowired
    EmployeeController employeeController;

    @Test
    void getEmployees() throws IOException {
        ResponseEntity<List<Employee>> entity = employeeController.getAllEmployees();
        assert entity.getBody().size() > 0;
    }

    @Test
    void getEmployeeByEmptyId() {
        ResponseEntity<Employee> entity = employeeController.getEmployeeById("");
        assert entity.getStatusCode().equals(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getEmployeeByNullId() {
        ResponseEntity<Employee> entity = employeeController.getEmployeeById(null);
        assert entity.getStatusCode().equals(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getHighestEmployeeSalary()  {
        ResponseEntity<Integer> entity = employeeController.getHighestSalaryOfEmployees();
        assert entity.getStatusCode().equals(HttpStatus.OK);
    }

    @Test
    void getTenHighestEmployeeSalary()  {
        ResponseEntity<List<String>> entity = employeeController.getTopTenHighestEarningEmployeeNames();
        assert entity.getStatusCode().equals(HttpStatus.OK);
    }

    @Test
    void createEmployeeWithNullRequest() {
        ResponseEntity<Employee> entity = employeeController.createEmployee(null);
        assert entity.getStatusCode().equals(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createEmployeeWithNullName() {
        ResponseEntity<Employee> entity = employeeController.createEmployee(new CreateEmployeeRequest(
                "","3","23"
        ));
        assert entity.getStatusCode().equals(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createEmployeeWithNullAge() {
        ResponseEntity<Employee> entity = employeeController.createEmployee(new CreateEmployeeRequest(
                "abc","3",""
        ));
        assert entity.getStatusCode().equals(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createEmployeeWithNullSalary() {
        ResponseEntity<Employee> entity = employeeController.createEmployee(new CreateEmployeeRequest(
                "abc","","34"
        ));
        assert entity.getStatusCode().equals(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createEmployee() {
        ResponseEntity<Employee> entity = employeeController.createEmployee(new CreateEmployeeRequest(
                "abc","20234","34"
        ));
        assert entity.getStatusCode().equals(HttpStatus.OK);
    }

    @Test
    void deleteEmployee() {
        ResponseEntity<String> entity = employeeController.deleteEmployeeById(null);
        assert entity.getStatusCode().equals(HttpStatus.BAD_REQUEST);
    }

}
