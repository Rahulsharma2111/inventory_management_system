package com.mis.StreamApi;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class EmployeeThreadService {
    private static final Random random = new Random();

    public static List<Employee> generateDemoEmployees() {

        List<Employee> employeeList = new ArrayList<>();

        String[] firstNames = {"John", "Jane", "Michael", "Sarah", "David", "Lisa", "Robert", "Emily", "William", "Jessica"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};
        String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose"};
        String[] states = {"CA", "TX", "NY", "FL", "IL", "PA", "OH", "GA", "NC", "MI"};
        String[] departments = {"IT", "HR", "Finance", "Marketing", "Sales", "Operations"};
        String[] positions = {"Software Engineer", "Manager", "Analyst", "Developer", "Consultant", "Specialist", "Director"};

        for (int i = 1; i <= 1000; i++) {
            Employee employee = new Employee();
            employee.setId((long) i);
            employee.setName(firstNames[random.nextInt(firstNames.length)] + " " + lastNames[random.nextInt(lastNames.length)]);
            employee.setCity(cities[random.nextInt(cities.length)]);
            employee.setState(states[random.nextInt(states.length)]);
//            employee.setSalary(40000 + random.nextDouble() * 110000);
            double salary = 10000 + random.nextDouble() * 20000;
            salary = Math.round(salary * 100.0) / 100.0; // Round to 2 decimal places
            employee.setSalary(salary);
            employee.setJobPosition(positions[random.nextInt(positions.length)]);
            employee.setDepartment(departments[random.nextInt(departments.length)]);

            int age = 22 + random.nextInt(43);
            int year = LocalDate.now().getYear() - age;
            int month = 1 + random.nextInt(12);
            int day = 1 + random.nextInt(28);
            employee.setDob(LocalDate.of(year, month, day));

            employeeList.add(employee);
        }

        return employeeList;
    }


    public List<Employee> processEmployeesWithTwoThreads() {

        List<Employee> employeeList = generateDemoEmployees();
        List<List<Employee>> splitList = splitEmployeeListITwoList(employeeList);
        System.out.println("first list after split " + splitList.get(0).size());
        System.out.println("second list after split " + splitList.get(1).size());

        List<Employee> processEmployeeList1 = processEmployeesDataUsingStreams(splitList.get(0));
        System.out.println("first list after process " + processEmployeeList1.size());
        List<Employee> processEmployeeList2 = processEmployeesDataUsingStreams(splitList.get(1));
        System.out.println("second list after process " + processEmployeeList2.size());

        List<Employee> combineList = combineBothEmployeeList(processEmployeeList1, processEmployeeList2);
        System.out.println("size of combineList: " + combineList.size());
        return combineList;
    }

    private List<Employee> combineBothEmployeeList(List<Employee> processEmployeeList1, List<Employee> processEmployeeList2) {
        System.out.println("In combine function");
        List<Employee> employeeList = new ArrayList<>();

        employeeList.addAll(processEmployeeList1);
        employeeList.addAll(processEmployeeList2);

//        for (int i = 0; i < processEmployeeList2.size(); i++) {
//            employeeList.add(processEmployeeList1.get(i));
//        }
//        processEmployeeList2.stream().map(employee ->  employeeList.add(employee)).toList();
        return employeeList;
    }

    @Async
    public List<Employee> processEmployeesDataUsingStreams(List<Employee> employees) {
        return employees.stream().filter(employee -> employee.getSalary() >= 15000).collect(Collectors.toList());
    }

    private List<List<Employee>> splitEmployeeListITwoList(List<Employee> employeeList) {
        int listSize = employeeList.size();
        int halfSize = listSize / 2;

        List<Employee> employeeList1 = new ArrayList<>();
        for (int i = 0; i < halfSize; i++) {
            employeeList1.add(employeeList.get(i));
        }

        List<Employee> employeeList2 = new ArrayList<>();
        for (int i = halfSize; i < listSize; i++) {
            employeeList2.add(employeeList.get(i));
        }
        List<List<Employee>> listOfEmployeeList = new ArrayList<>();
        listOfEmployeeList.add(employeeList1);
        listOfEmployeeList.add(employeeList2);
        return listOfEmployeeList;
    }
}



/*
package com.mis.StreamApi;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class EmployeeThreadService {
    private static final Random random = new Random();

    public static List<Employee> generateDemoEmployees() {

        List<Employee> employeeList = new ArrayList<>();

        String[] firstNames = {"John", "Jane", "Michael", "Sarah", "David", "Lisa", "Robert", "Emily", "William", "Jessica"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};
        String[] cities = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix", "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose"};
        String[] states = {"CA", "TX", "NY", "FL", "IL", "PA", "OH", "GA", "NC", "MI"};
        String[] departments = {"IT", "HR", "Finance", "Marketing", "Sales", "Operations"};
        String[] positions = {"Software Engineer", "Manager", "Analyst", "Developer", "Consultant", "Specialist", "Director"};

        for (int i = 1; i <= 1000; i++) {
            Employee employee = new Employee();
            employee.setId((long) i);
            employee.setName(firstNames[random.nextInt(firstNames.length)] + " " + lastNames[random.nextInt(lastNames.length)]);
            employee.setCity(cities[random.nextInt(cities.length)]);
            employee.setState(states[random.nextInt(states.length)]);
//            employee.setSalary(40000 + random.nextDouble() * 110000);
            double salary = 10000 + random.nextDouble() * 20000;
            salary = Math.round(salary * 100.0) / 100.0; // Round to 2 decimal places
            employee.setSalary(salary);
            employee.setJobPosition(positions[random.nextInt(positions.length)]);
            employee.setDepartment(departments[random.nextInt(departments.length)]);

            int age = 22 + random.nextInt(43);
            int year = LocalDate.now().getYear() - age;
            int month = 1 + random.nextInt(12);
            int day = 1 + random.nextInt(28);
            employee.setDob(LocalDate.of(year, month, day));

            employeeList.add(employee);
        }

        return employeeList;
    }

    public List<Employee> processEmployeesWithTwoThreads() throws ExecutionException, InterruptedException {

        List<Employee> employeeList = generateDemoEmployees();
        List<List<Employee>> splitList = splitEmployeeListITwoList(employeeList);
        System.out.println("first list after split " + splitList.get(0).size());
        System.out.println("second list after split " + splitList.get(1).size());

        CompletableFuture<List<Employee>> future1 = processEmployeesDataUsingStreams(splitList.get(0));
        CompletableFuture<List<Employee>> future2 = processEmployeesDataUsingStreams(splitList.get(1));

        CompletableFuture.allOf(future1, future2).join();

        List<Employee> processEmployeeList1 = future1.get();
        System.out.println("first list after process " + processEmployeeList1.size());
        List<Employee> processEmployeeList2 = future2.get();

        System.out.println("second list after process " + processEmployeeList2.size());

        List<Employee> combineList = combineBothEmployeeList(processEmployeeList1, processEmployeeList2);
        System.out.println("size of combineList: " + combineList.size());
        return combineList;
    }

    private List<Employee> combineBothEmployeeList(List<Employee> processEmployeeList1, List<Employee> processEmployeeList2) {
        System.out.println("In combine function");
        List<Employee> employeeList = new ArrayList<>();

        employeeList.addAll(processEmployeeList1);
        employeeList.addAll(processEmployeeList2);

//        for (int i = 0; i < processEmployeeList2.size(); i++) {
//            employeeList.add(processEmployeeList1.get(i));
//        }
//        processEmployeeList2.stream().map(employee ->  employeeList.add(employee)).toList();
        return employeeList;
    }

    @Async
    public CompletableFuture<List<Employee>> processEmployeesDataUsingStreams(List<Employee> employees) {
        System.out.println("Processing in thread: " + Thread.currentThread().getName() + " - List size: " + employees.size());

        List<Employee> result= employees.stream().filter(employee -> employee.getSalary() >= 15000).collect(Collectors.toList());
        return CompletableFuture.completedFuture(result);
    }

    private List<List<Employee>> splitEmployeeListITwoList(List<Employee> employeeList) {
        int listSize = employeeList.size();
        int halfSize = listSize / 2;

        List<Employee> employeeList1 = new ArrayList<>();
        for (int i = 0; i < halfSize; i++) {
            employeeList1.add(employeeList.get(i));
        }

        List<Employee> employeeList2 = new ArrayList<>();
        for (int i = halfSize; i < listSize; i++) {
            employeeList2.add(employeeList.get(i));
        }
        List<List<Employee>> listOfEmployeeList = new ArrayList<>();
        listOfEmployeeList.add(employeeList1);
        listOfEmployeeList.add(employeeList2);
        return listOfEmployeeList;
    }
}
*/
