# Employee Performance Tracker System (ETS)

A production-ready, full-stack Java enterprise application designed for tracking employee metrics, managing developmental goals, organizing attendance logs, and evaluating performance scorecards. The application features an automated Promotion Recommendation engine, robust reporting exports (Excel, PDF, CSV), and a responsive visual dashboard.

---

## Tech Stack

### Backend
* **Java 21**
* **Spring Boot 3.3.x**
* **Spring Security** (Form-based authentication, path authorization filters)
* **Spring Data JPA** (Hibernate)
* **Spring Validation** (Request body boundary constraint annotations)
* **ModelMapper** (DTO encapsulation mapping layers)
* **Lombok** (Boilerplate reduction)
* **Apache POI & OpenPDF** (Dynamic document generation)
* **Maven** (Dependency builder)

### Database
* **MySQL 8+** (Default production)
* **H2 Database** (Local preview / simulation fallback profile)

### Frontend
* **HTML5** & **Thymeleaf 3.1**
* **CSS3 & Bootstrap 5** (Premium styling themes, dark mode variables)
* **Vanilla JavaScript** (Chart rendering, incremental animations, asynchronous API client queries)
* **Chart.js** (Interactive statistics, distributions, and history charts)

---

## Key Features

1. **Analytical Dashboard:** Displays dynamic counter cards (increments from zero), list of promotion recommendations, and 4 animated Chart.js cards (Department employee distribution, Skill distribution, Performance Rating averages, and Monthly Attendance rates).
2. **Directory Directories:** Paginated, sortable listing of employees featuring a global ajax-powered search box looking up name, code, designation, or department.
3. **Performance Scorecard Matrix:** Multi-metric scoring sliders (Technical, Communication, Teamwork, Leadership, Attendance, Problem Solving, Innovation, Discipline) automatically calculating averages and categories.
4. **Goal Management System:** Add and update goals complete with target deadlines, progress sliders, and status changes.
5. **Promotion Recommendation Engine:** Evaluates tenure and ratings to suggest candidate promotions (Tenure $\ge$ 2 years, Scorecard Average > 85%, Attendance > 95%, pending goals == 0).
6. **Reporting & Exporters:**
   * **CSV Exporter:** Fast plain-text streams.
   * **Excel Exporter:** Custom Styled worksheets using Apache POI.
   * **PDF Exporter:** Multi-page landscape PDF grid tables using OpenPDF.

---

## Setup & Running

### Prerequisites
* Java Development Kit (JDK) 21
* Apache Maven 3.9+
* MySQL Server (Optional, only if using the default profile)

---

### Option A: Running with In-Memory Database (No Database Setup Needed)
Ideal for quick local previews and reviews:
1. Open terminal in the project directory.
2. Run the Maven start command with active `h2` profile:
   ```bash
   mvn spring-boot:run "-Dspring-boot.run.profiles=h2"
   ```
3. Open your browser and navigate to: **[http://localhost:8085/](http://localhost:8085/)**

---

### Option B: Running with MySQL 8+ (Default Profile)
1. Ensure your MySQL server is running.
2. Create the target schema database:
   ```sql
   CREATE DATABASE employee_performance_tracker;
   ```
3. Update connection parameters (Username, Password, and Dialect) in `src/main/resources/application.properties`.
4. Run the start command:
   ```bash
   mvn spring-boot:run
   ```
5. Open your browser and navigate to: **[http://localhost:8085/](http://localhost:8085/)**

---

## Seed Accounts (Seeded automatically on startup)

Access the system using any of the default demo credentials:

| Username | Password | Role | Access Level |
| :--- | :--- | :--- | :--- |
| `admin` | `admin` | `ROLE_ADMIN` | Full admin privileges (System directory controls) |
| `hr` | `hr` | `ROLE_HR` | HR personnel rights (Attendance / directories) |
| `manager` | `manager` | `ROLE_MANAGER` | Review manager rights (Goal updates, Performance scorecards) |

---

## Testing REST APIs
Import the Postman collection template `employee_performance_tracker_postman_collection.json` located in the root directory to verify REST endpoints, authentication cookies, data updates, and report download links.
