package com.mis.StreamApi;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeThreadService employeeThreadService;

    @GetMapping("/process")
    public List<Employee> processEmployees() throws ExecutionException, InterruptedException {
        System.out.println("Starting employee processing with salary filter...");
        return employeeThreadService.processEmployeesWithTwoThreads();
    }

    @GetMapping("/test")
    public String test() {
        return "Employee Processing API is running! Use /api/employees/process to start processing.";
    }
}