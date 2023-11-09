package com.example.rqchallenge.util;

import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.dto.response.EmployeeResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataGeneratorUtils {
    public static EmployeeResponse getEmployees(){
        EmployeeResponse employeeResponse = new EmployeeResponse();
        Employee employee1 = new Employee(
                "Rohan Deshmukh",
                "50000",
                "25",
                1l
        );
        Employee employee2 = new Employee(
                "Rohit Sharma",
                "60000",
                "26",
                2l
        );
        employeeResponse.setData(Arrays.asList(employee1,employee2));
        return employeeResponse;
    }

    public static Employee getEmployee(){
        Employee employee1 = new Employee(
                "Rohan Deshmukh",
                "50000",
                "25",
                1l
        );
        return employee1;
    }
}
