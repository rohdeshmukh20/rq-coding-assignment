package com.example.rqchallenge.util;

import com.example.rqchallenge.dto.Employee;

public class EmployeeUtils {
    public static Integer parseSalary (Employee employee) {
        try {
            return Integer.parseInt(employee.getEmployee_salary());
        } catch (Exception exception) {
            return 0;
        }
    }
}
