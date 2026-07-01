package com.company.employeetracker.config;

import com.company.employeetracker.entity.*;
import com.company.employeetracker.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final SkillRepository skillRepository;
    private final EmployeeSkillRepository employeeSkillRepository;
    private final GoalRepository goalRepository;
    private final AttendanceRepository attendanceRepository;
    private final PerformanceReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(RoleRepository roleRepository,
                          UserRepository userRepository,
                          DepartmentRepository departmentRepository,
                          EmployeeRepository employeeRepository,
                          SkillRepository skillRepository,
                          EmployeeSkillRepository employeeSkillRepository,
                          GoalRepository goalRepository,
                          AttendanceRepository attendanceRepository,
                          PerformanceReviewRepository reviewRepository,
                          PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.skillRepository = skillRepository;
        this.employeeSkillRepository = employeeSkillRepository;
        this.goalRepository = goalRepository;
        this.attendanceRepository = attendanceRepository;
        this.reviewRepository = reviewRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. Seed Roles
        Role adminRole = seedRole("ROLE_ADMIN");
        Role hrRole = seedRole("ROLE_HR");
        Role managerRole = seedRole("ROLE_MANAGER");

        // 2. Seed Users
        seedUser("admin", "admin", adminRole);
        seedUser("hr", "hr", hrRole);
        User managerUser = seedUser("manager", "manager", managerRole);

        // 3. Seed Departments
        if (departmentRepository.count() == 0) {
            List<Department> departments = Arrays.asList(
                Department.builder().name("Technology").manager("Alex Carter").location("New York").description("IT and Software Engineering").build(),
                Department.builder().name("Human Resources").manager("Sarah Jenkins").location("Chicago").description("Talent acquisition and employee relations").build(),
                Department.builder().name("Finance").manager("David Vance").location("London").description("Corporate finance and budgeting").build(),
                Department.builder().name("Sales").manager("Rachel Green").location("San Francisco").description("Enterprise sales and marketing").build(),
                Department.builder().name("Marketing").manager("Emma Watson").location("Boston").description("Brand management and campaigns").build()
            );
            departmentRepository.saveAll(departments);
        }

        // 4. Seed Employees
        if (employeeRepository.count() == 0) {
            List<Department> allDepts = departmentRepository.findAll();
            List<String> maleFirstNames = Arrays.asList("James", "John", "Robert", "Michael", "William", "David", "Richard", "Joseph", "Thomas", "Charles", "Christopher", "Daniel", "Matthew", "Anthony", "Mark");
            List<String> femaleFirstNames = Arrays.asList("Mary", "Patricia", "Jennifer", "Linda", "Elizabeth", "Barbara", "Susan", "Jessica", "Sarah", "Karen", "Lisa", "Nancy", "Betty", "Sandra", "Ashley");
            List<String> lastNames = Arrays.asList("Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin");
            List<String> designations = Arrays.asList("Software Engineer", "Senior Software Engineer", "Tech Lead", "HR Executive", "Financial Analyst", "Sales Manager", "Marketing Executive", "Product Manager", "QA Engineer", "Database Administrator");
            
            Random random = new Random();
            List<Employee> employees = new ArrayList<>();

            for (int i = 1; i <= 50; i++) {
                String gender = random.nextBoolean() ? "Male" : "Female";
                String firstName = gender.equals("Male") 
                        ? maleFirstNames.get(random.nextInt(maleFirstNames.size())) 
                        : femaleFirstNames.get(random.nextInt(femaleFirstNames.size()));
                String lastName = lastNames.get(random.nextInt(lastNames.size()));
                String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + i + "@company.com";
                String code = String.format("EMP%03d", i);
                String phone = "+1-" + (100 + random.nextInt(900)) + "-" + (100 + random.nextInt(900)) + "-" + (1000 + random.nextInt(9000));
                
                LocalDate dob = LocalDate.now().minusYears(22 + random.nextInt(28)).minusDays(random.nextInt(365));
                // Experience: 1 to 5 years ago
                LocalDate joinDate = LocalDate.now().minusYears(1 + random.nextInt(4)).minusDays(random.nextInt(365));
                
                Department dept = allDepts.get(random.nextInt(allDepts.size()));
                String designation = designations.get(random.nextInt(designations.size()));
                Double salary = 40000.0 + random.nextInt(110000);
                String status = "ACTIVE"; // Seed active for testing promotion recommendations

                Employee emp = Employee.builder()
                        .employeeCode(code)
                        .firstName(firstName)
                        .lastName(lastName)
                        .email(email)
                        .phone(phone)
                        .gender(gender)
                        .dateOfBirth(dob)
                        .joiningDate(joinDate)
                        .designation(designation)
                        .salary(salary)
                        .status(status)
                        .department(dept)
                        .build();
                employees.add(emp);
            }
            employeeRepository.saveAll(employees);
        }

        // 5. Seed Skills
        List<Skill> skills = new ArrayList<>();
        if (skillRepository.count() == 0) {
            skills = Arrays.asList(
                Skill.builder().name("Java").description("Core Java, Multithreading, Streams").build(),
                Skill.builder().name("Spring Boot").description("REST APIs, JPA, Security").build(),
                Skill.builder().name("SQL").description("Database design, querying, indexes").build(),
                Skill.builder().name("JavaScript").description("ES6+, DOM, Fetch API").build(),
                Skill.builder().name("HTML/CSS").description("Semantic design and Bootstrap").build(),
                Skill.builder().name("Git").description("Version control, branch workflows").build(),
                Skill.builder().name("Project Management").description("Agile, Scrum, Planning").build(),
                Skill.builder().name("Communication").description("Verbal and written presentation").build()
            );
            skills = skillRepository.saveAll(skills);
        } else {
            skills = skillRepository.findAll();
        }

        // Fetch employees and seed relationship details
        List<Employee> allEmployees = employeeRepository.findAll();
        Random random = new Random();

        // 6. Seed Employee Skills
        if (employeeSkillRepository.count() == 0 && !skills.isEmpty()) {
            List<String> levels = Arrays.asList("BEGINNER", "INTERMEDIATE", "ADVANCED", "EXPERT");
            for (Employee emp : allEmployees) {
                // Assign 2-3 random skills
                Collections.shuffle(skills);
                int skillCount = 2 + random.nextInt(2);
                for (int s = 0; s < skillCount; s++) {
                    employeeSkillRepository.save(EmployeeSkill.builder()
                            .employee(emp)
                            .skill(skills.get(s))
                            .skillLevel(levels.get(random.nextInt(levels.size())))
                            .build());
                }
            }
        }

        // 7. Seed Attendance Logs (2-3 months logs for each employee)
        if (attendanceRepository.count() == 0) {
            List<String> months = Arrays.asList("2026-05", "2026-06", "2026-07");
            for (Employee emp : allEmployees) {
                for (String month : months) {
                    // Seed realistic attendance (92% - 100%)
                    double pct = 92.0 + (random.nextDouble() * 8.0);
                    attendanceRepository.save(Attendance.builder()
                            .employee(emp)
                            .attendancePercentage(pct)
                            .monthYear(month)
                            .build());
                }
            }
        }

        // 8. Seed Goals
        if (goalRepository.count() == 0) {
            List<String> goalNames = Arrays.asList("Optimize DB queries", "Complete security training", "Migrate API schemas", "Design user dashboard", "Deploy code module");
            List<String> statuses = Arrays.asList("PENDING", "IN_PROGRESS", "COMPLETED");
            for (Employee emp : allEmployees) {
                // Assign 1-2 goals
                int goalCount = 1 + random.nextInt(2);
                for (int g = 0; g < goalCount; g++) {
                    String status = statuses.get(random.nextInt(statuses.size()));
                    int progress = status.equals("COMPLETED") ? 100 : (status.equals("IN_PROGRESS") ? 10 + random.nextInt(80) : 0);
                    goalRepository.save(Goal.builder()
                            .employee(emp)
                            .goalName(goalNames.get(random.nextInt(goalNames.size())))
                            .deadline(LocalDate.now().plusMonths(1 + random.nextInt(3)))
                            .progress(progress)
                            .status(status)
                            .build());
                }
            }
        }

        // 9. Seed Performance Reviews
        if (reviewRepository.count() == 0 && managerUser != null) {
            for (Employee emp : allEmployees) {
                // Let's seed 1 review for each employee
                int scoreTech = 70 + random.nextInt(31);
                int scoreComm = 70 + random.nextInt(31);
                int scoreTeam = 70 + random.nextInt(31);
                int scoreLead = 70 + random.nextInt(31);
                int scoreAtten = 80 + random.nextInt(21);
                int scoreProb = 70 + random.nextInt(31);
                int scoreInnov = 70 + random.nextInt(31);
                int scoreDisc = 80 + random.nextInt(21);

                double total = scoreTech + scoreComm + scoreTeam + scoreLead + scoreAtten + scoreProb + scoreInnov + scoreDisc;
                double avg = total / 8.0;

                String rating;
                if (avg >= 90.0) rating = "Excellent";
                else if (avg >= 80.0) rating = "Very Good";
                else if (avg >= 70.0) rating = "Good";
                else if (avg >= 50.0) rating = "Average";
                else rating = "Needs Improvement";

                reviewRepository.save(PerformanceReview.builder()
                        .employee(emp)
                        .manager(managerUser)
                        .technicalKnowledge(scoreTech)
                        .communication(scoreComm)
                        .teamwork(scoreTeam)
                        .leadership(scoreLead)
                        .attendance(scoreAtten)
                        .problemSolving(scoreProb)
                        .innovation(scoreInnov)
                        .discipline(scoreDisc)
                        .remarks("Seeded review by system manager.")
                        .reviewDate(LocalDate.now().minusMonths(1).minusDays(random.nextInt(15)))
                        .overallScore(avg)
                        .overallRating(rating)
                        .build());
            }
        }
    }

    private Role seedRole(String name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(Role.builder().name(name).build()));
    }

    private User seedUser(String username, String password, Role role) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.save(User.builder()
                        .username(username)
                        .password(passwordEncoder.encode(password))
                        .enabled(true)
                        .roles(new HashSet<>(Collections.singletonList(role)))
                        .build()));
    }
}
