//package com.mis.StreamApi;
//
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.*;
//import java.util.concurrent.CompletableFuture;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//@Service
//public class EmployeeService {
//
//    // Enhanced data lists
//    private final List<String> names = Arrays.asList(
//            "John Smith", "Jane Doe", "Michael Johnson", "Sarah Williams", "David Brown",
//            "Lisa Garcia", "Robert Miller", "Emily Davis", "James Wilson", "Maria Martinez",
//            "William Anderson", "Linda Taylor", "Christopher Thomas", "Susan Moore", "Daniel Jackson",
//            "Karen Lee", "Matthew Harris", "Nancy Clark", "Anthony Lewis", "Betty Robinson",
//            "Mark Walker", "Sandra Hall", "Donald Allen", "Donna Young", "Paul King",
//            "Carol Wright", "Kevin Scott", "Ruth Green", "Brian Adams", "Sharon Baker",
//            "George Nelson", "Cynthia Carter", "Edward Mitchell", "Kathleen Perez", "Ronald Roberts",
//            "Deborah Turner", "Jason Phillips", "Dorothy Campbell", "Jeffrey Parker", "Jessica Evans",
//            "Ryan Edwards", "Michelle Collins", "Gary Stewart", "Amanda Sanchez", "Timothy Morris",
//            "Helen Rogers", "Joshua Reed", "Laura Cook", "Kenneth Morgan", "Donna Bell",
//            "Stephen Murphy", "Rachel Bailey", "Jacob Rivera", "Martha Cooper", "Patrick Richardson",
//            "Brenda Cox", "Nathan Howard", "Pamela Ward", "Samuel Torres", "Tiffany Peterson",
//            "Benjamin Gray", "Rebecca Ramirez", "Gregory James", "Cheryl Watson", "Frank Brooks",
//            "Heather Kelly", "Raymond Sanders", "Evelyn Price", "Scott Bennett", "Tina Wood",
//            "Alexander Barnes", "Katherine Ross", "Patrick Henderson", "Gloria Coleman", "Dennis Jenkins",
//            "Victoria Perry", "Jerry Powell", "Judith Long", "Henry Patterson", "Beverly Hughes",
//            "Carl Flores", "Megan Washington", "Arthur Butler", "Janet Simmons", "Ryan Foster",
//            "Catherine Gonzales", "Ralph Bryant", "Diane Alexander", "Eugene Russell", "Alice Griffin",
//            "Wayne Diaz", "Marie Hayes", "Bruce Myers", "Joan Ford", "Alan Hamilton"
//    );
//
//    private final List<String> cities = Arrays.asList(
//            "New York", "Los Angeles", "Chicago", "Houston", "Phoenix",
//            "Philadelphia", "San Antonio", "San Diego", "Dallas", "San Jose",
//            "Austin", "Jacksonville", "Fort Worth", "Columbus", "Indianapolis",
//            "Charlotte", "San Francisco", "Seattle", "Denver", "Washington",
//            "Boston", "El Paso", "Nashville", "Detroit", "Oklahoma City",
//            "Portland", "Las Vegas", "Memphis", "Louisville", "Baltimore",
//            "Milwaukee", "Albuquerque", "Tucson", "Fresno", "Sacramento",
//            "Mesa", "Kansas City", "Atlanta", "Long Beach", "Omaha",
//            "Raleigh", "Miami", "Virginia Beach", "Oakland", "Minneapolis",
//            "Tulsa", "Arlington", "New Orleans", "Wichita", "Cleveland",
//            "Tampa", "Bakersfield", "Aurora", "Anaheim", "Honolulu",
//            "Santa Ana", "Riverside", "Corpus Christi", "Lexington", "Stockton",
//            "St. Louis", "Saint Paul", "Henderson", "Pittsburgh", "Cincinnati",
//            "Anchorage", "Greensboro", "Plano", "Newark", "Lincoln"
//    );
//
//    private final List<String> states = Arrays.asList(
//            "AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA",
//            "HI", "ID", "IL", "IN", "IA", "KS", "KY", "LA", "ME", "MD",
//            "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ",
//            "NM", "NY", "NC", "ND", "OH", "OK", "OR", "PA", "RI", "SC",
//            "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY"
//    );
//
//    private final List<String> positions = Arrays.asList(
//            "Software Engineer", "Senior Developer", "Frontend Developer", "Backend Developer", "Full Stack Developer",
//            "DevOps Engineer", "Data Scientist", "Data Analyst", "Machine Learning Engineer", "AI Specialist",
//            "Cloud Architect", "Systems Administrator", "Network Engineer", "Database Administrator", "IT Manager",
//            "Project Manager", "Product Manager", "Scrum Master", "Business Analyst", "Technical Lead",
//            "QA Engineer", "Test Automation Engineer", "Security Analyst", "Cybersecurity Specialist", "IT Director",
//            "CTO", "CIO", "Solutions Architect", "UX Designer", "UI Designer",
//            "Product Designer", "Graphic Designer", "Web Designer", "Mobile Developer", "iOS Developer",
//            "Android Developer", "React Developer", "Java Developer", "Python Developer", "JavaScript Developer",
//            "Node.js Developer", "React Native Developer", "Flutter Developer", ".NET Developer", "PHP Developer",
//            "Ruby on Rails Developer", "Go Developer", "Rust Developer", "Scala Developer", "Sales Manager",
//            "Account Executive", "Business Development Manager", "Sales Representative", "Account Manager",
//            "Marketing Manager", "Digital Marketing Specialist", "Content Marketing Manager", "SEO Specialist",
//            "Social Media Manager", "Brand Manager", "Financial Analyst", "Investment Banker", "Accountant",
//            "Financial Controller", "CFO", "HR Manager", "Recruiter", "Talent Acquisition Specialist",
//            "HR Business Partner", "Compensation Analyst", "Training Manager", "Operations Manager",
//            "Supply Chain Manager", "Logistics Coordinator", "Procurement Specialist", "Customer Service Manager",
//            "Technical Support Specialist", "Help Desk Analyst", "Research Scientist", "Lab Technician",
//            "Clinical Research Coordinator", "Healthcare Administrator", "Nurse Manager", "Medical Director"
//    );
//
//    private final List<String> departments = Arrays.asList(
//            "Software Engineering", "Frontend Development", "Backend Development", "Mobile Development", "DevOps",
//            "Cloud Infrastructure", "Data Science", "Machine Learning", "Artificial Intelligence", "Quality Assurance",
//            "IT Operations", "Network Security", "Cybersecurity", "Database Management", "System Administration",
//            "Project Management Office", "Product Management", "Business Analysis", "Agile Coaching", "Technical Leadership",
//            "UX/UI Design", "Graphic Design", "Product Design", "User Research", "Sales Department",
//            "Business Development", "Account Management", "Enterprise Sales", "Channel Sales", "Marketing",
//            "Digital Marketing", "Content Marketing", "Social Media", "Brand Management", "Public Relations",
//            "Finance", "Accounting", "Financial Planning", "Investment Management", "Audit",
//            "Human Resources", "Talent Acquisition", "Learning & Development", "Compensation & Benefits", "HR Operations",
//            "Operations Management", "Supply Chain", "Logistics", "Procurement", "Facilities Management",
//            "Customer Service", "Technical Support", "Client Success", "Customer Experience", "Research & Development",
//            "Clinical Research", "Healthcare Services", "Medical Affairs", "Legal Department", "Compliance",
//            "Risk Management", "Internal Audit", "Corporate Strategy", "Business Intelligence"
//    );
//
//    // Generate 1000 employees with realistic salary ranges based on position
//    public List<Employee> generateEmployees() {
//        Random random = new Random();
//
//        return IntStream.rangeClosed(1, 1000)
//                .mapToObj(i -> {
//                    String name = names.get(random.nextInt(names.size()));
//                    String city = cities.get(random.nextInt(cities.size()));
//                    String state = states.get(random.nextInt(states.size()));
//                    String position = positions.get(random.nextInt(positions.size()));
//                    String department = getDepartmentForPosition(position);
//
//                    // Generate realistic salary based on position
//                    double salary = generateRealisticSalary(position, random);
//
//                    // Generate realistic age based on position (22-65)
//                    int age = generateRealisticAge(position, random);
//                    LocalDate dob = LocalDate.now().minusYears(age).minusMonths(random.nextInt(12));
//
//                    return new Employee((long) i, name, city, state, salary, position, dob, department);
//                })
//                .collect(Collectors.toList());
//    }
//
//    private String getDepartmentForPosition(String position) {
//
//        if (position.contains("Developer") || position.contains("Engineer") ||
//                position.contains("Architect") || position.contains("CTO") || position.contains("CIO")) {
//            return "Software Engineering";
//        } else if (position.contains("Data") || position.contains("AI") || position.contains("Machine Learning")) {
//            return "Data Science";
//        } else if (position.contains("Designer") || position.contains("UX") || position.contains("UI")) {
//            return "UX/UI Design";
//        } else if (position.contains("Manager") && position.contains("Project")) {
//            return "Project Management Office";
//        } else if (position.contains("Manager") && position.contains("Product")) {
//            return "Product Management";
//        } else if (position.contains("Sales") || position.contains("Account")) {
//            return "Sales Department";
//        } else if (position.contains("Marketing")) {
//            return "Marketing";
//        } else if (position.contains("Finance") || position.contains("Accountant") || position.contains("CFO")) {
//            return "Finance";
//        } else if (position.contains("HR") || position.contains("Recruiter") || position.contains("Talent")) {
//            return "Human Resources";
//        } else if (position.contains("Operations") || position.contains("Supply Chain") || position.contains("Logistics")) {
//            return "Operations Management";
//        } else if (position.contains("Support") || position.contains("Help Desk") || position.contains("Customer")) {
//            return "Customer Service";
//        } else {
//            return departments.get(new Random().nextInt(departments.size()));
//        }
//    }
//
//    private double generateRealisticSalary(String position, Random random) {
//        if (position.contains("CTO") || position.contains("CIO") || position.contains("Director")) {
//            return 150000 + (random.nextDouble() * 100000); // $150k - $250k
//        } else if (position.contains("Senior") || position.contains("Lead") || position.contains("Manager")) {
//            return 90000 + (random.nextDouble() * 60000); // $90k - $150k
//        } else if (position.contains("Engineer") || position.contains("Developer") || position.contains("Analyst")) {
//            return 60000 + (random.nextDouble() * 50000); // $60k - $110k
//        } else if (position.contains("Junior") || position.contains("Entry")) {
//            return 40000 + (random.nextDouble() * 20000);
//        } else {
//            return 50000 + (random.nextDouble() * 70000);
//        }
//    }
//
//    private int generateRealisticAge(String position, Random random) {
//        if (position.contains("CTO") || position.contains("CIO") || position.contains("Director")) {
//            return 40 + random.nextInt(25); // 40-65
//        } else if (position.contains("Senior") || position.contains("Lead") || position.contains("Manager")) {
//            return 30 + random.nextInt(15); // 30-45
//        } else if (position.contains("Junior") || position.contains("Entry")) {
//            return 22 + random.nextInt(8); // 22-30
//        } else {
//            return 25 + random.nextInt(20); // 25-45 (default range)
//        }
//    }
//
//    // Filter employees with salary >= 10000
//    public List<Employee> filterBySalary(List<Employee> employees) {
//        return employees.stream()
//                .filter(emp -> emp.getSalary() >= 10000)
//                .collect(Collectors.toList());
//    }
//
//    // Split list into two parts for two threads
//    public Map<String, List<Employee>> splitEmployees(List<Employee> employees) {
//        int mid = employees.size() / 2;
//        return Map.of(
//                "thread1", employees.subList(0, mid),
//                "thread2", employees.subList(mid, employees.size())
//        );
//    }
//
//    @Async
//    public CompletableFuture<List<Employee>> processEmployeeChunk(List<Employee> employees, String threadName) {
//        long startTime = System.currentTimeMillis();
//
//        System.out.println(threadName + " started processing " + employees.size() + " employees");
//
//        // Process employees with salary >= 10000 using stream
//        List<Employee> highSalaryEmployees = employees.stream()
//                .filter(emp -> emp.getSalary() >= 10000)
//                .map(this::enrichEmployeeData)
//                .collect(Collectors.toList());
//
//        long processingTime = System.currentTimeMillis() - startTime;
//
//        System.out.println(threadName + " completed in " + processingTime + "ms. Processed: " + highSalaryEmployees.size() + " employees");
//
//        return CompletableFuture.completedFuture(highSalaryEmployees);
//    }
//
//    private Employee enrichEmployeeData(Employee emp) {
//        // Simulate some processing
//        try {
//            Thread.sleep(1);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//
//        return emp;
//    }
//
//    // Main method to process all employees with two threads and return List<Employee>
//    public List<Employee> processEmployeesWithTwoThreads() {
//        long totalStartTime = System.currentTimeMillis();
//
//        // Generate employees
//        List<Employee> allEmployees = generateEmployees();
//        System.out.println("Generated " + allEmployees.size() + " employees with diverse data");
//
//        // Filter employees with salary >= 10000
//        List<Employee> filteredEmployees = filterBySalary(allEmployees);
//        System.out.println("Employees with salary >= $10,000: " + filteredEmployees.size());
//
//        if (filteredEmployees.isEmpty()) {
//            return new ArrayList<>(); // Return empty list instead of Map
//        }
//
//        // Split filtered employees for two threads
//        Map<String, List<Employee>> splitData = splitEmployees(filteredEmployees);
//
//        // Start both threads
//        CompletableFuture<List<Employee>> future1 =
//                processEmployeeChunk(splitData.get("thread1"), "Thread-1");
//
//        CompletableFuture<List<Employee>> future2 =
//                processEmployeeChunk(splitData.get("thread2"), "Thread-2");
//
////        // Wait for both threads to complete
//        CompletableFuture.allOf(future1, future2).join();
//
//        try {
//            List<Employee> result1 = future1.get();
//            List<Employee> result2 = future2.get();
//
//            return combineResults(result1, result2);
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error processing employees", e);
//        }
//    }
//
//    private List<Employee> combineResults(List<Employee> result1, List<Employee> result2) {
//        List<Employee> combinedList = new ArrayList<>();
//        combinedList.addAll(result1);
//        combinedList.addAll(result2);
//        return combinedList;
//    }
//
// /*   // Additional method to get filtered employees without threading (simpler version)
//    public List<Employee> getHighSalaryEmployees() {
//        List<Employee> allEmployees = generateEmployees();
//        return filterBySalary(allEmployees);
//    }
//
//    // Method to get employees with salary in specific range
//    public List<Employee> getEmployeesBySalaryRange(double minSalary, double maxSalary) {
//        List<Employee> allEmployees = generateEmployees();
//        return allEmployees.stream()
//                .filter(emp -> emp.getSalary() >= minSalary && emp.getSalary() <= maxSalary)
//                .collect(Collectors.toList());
//    }
//
//    // Method to get employees by department with salary filter
//    public List<Employee> getEmployeesByDepartmentWithMinSalary(String department, double minSalary) {
//        List<Employee> allEmployees = generateEmployees();
//        return allEmployees.stream()
//                .filter(emp -> emp.getDepartment().equals(department) && emp.getSalary() >= minSalary)
//                .collect(Collectors.toList());
//    }*/
//}