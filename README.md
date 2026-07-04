# Library App — Backend

Two Spring Boot microservices for the Library App:

| Service             | Port | Role                                                                                                                                                         |
|---------------------|------|--------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **library-service** | 8080 | Frontend-facing API gateway/orchestrator. JWT-secured. Contains `@RestController` and business logic, including sending and broadcasting live notifications. |
| **domain-service**  | 8081 | Data layer service. Exposes CRUD endpoints using `@RepositoryRestResource` and custom aggregate queries. Owns the MySQL database.                            |

**Technology Stack**

* Java 25
* Spring Boot 4.1
* Spring MVC (running each request on a virtual thread)
* Spring Data REST
* Spring Data JPA
* MySQL
* Lombok
* JWT (jjwt)
* springdoc-openapi
* JUnit 5 & Mockito

---

# Getting Started

## Prerequisites

Install the following software before running the application:

| Software        | Version        | Notes                                                   |
|-----------------|----------------|---------------------------------------------------------|
| Java            | 25             | Required to run both Spring Boot services.              |
| Apache Maven    | 3.9.9 or later | Used to build and run the project.                      |
| MySQL Server    | Latest         | Database used by the application.                       |
| MySQL Workbench | Latest         | Recommended for managing the MySQL server.              |
| DBeaver         | Latest         | Optional database client for viewing and querying data. |
| IntelliJ IDEA   | Latest         | Recommended IDE for opening both backend projects.      |

---

## 1. Install Java 25

Download and install Java 25.

After installation, verify that Java is correctly configured by opening a terminal and running:

```bash
java -version
javac -version
```

Ensure the `JAVA_HOME` environment variable is configured and that Java has been added to your system `PATH`.

---

## 2. Install Maven

Install Apache Maven **3.9.9 or later**.

Verify the installation:

```bash
mvn -version
```

The output should display both the Maven version and Java 25.

---

## 3. Install MySQL

Install:

* MySQL Server
* MySQL Workbench

During installation, create a MySQL administrator account (for example `admin`) and remember the password.

Create a database named:

```
library_app
```

Alternatively, the application can automatically create the database if the configured user has sufficient privileges.

---

## 4. Configure the Database

Open the following file inside **domain-service**:

```
src/main/resources/application.properties
```

Update the database credentials to match your local MySQL installation:

```properties
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
```

Example:

```properties
spring.datasource.username=admin
spring.datasource.password=password123
```

---

## 5. Enable Data Seeding

Ensure the following property is enabled inside `application.properties`:

```properties
app.data-seed.enabled=${DATA_SEED_ENABLED:true}
```

When enabled, the application will automatically populate the database with sample data on startup.

---

## 6. Import the Projects

Open IntelliJ IDEA.

Import both projects as **Maven Projects**:

* library-service
* domain-service

Allow IntelliJ to download all Maven dependencies.

---

## 7. Start the Backend Services

Start the services in the following order:

### Step 1

Run:

```
DomainServiceApplication
```

This starts the data service on **http://localhost:8081**.

### Step 2

Run:

```
LibraryServiceApplication
```

This starts the API service on **http://localhost:8080**.

---

## 8. Run the Angular Frontend

Open the Angular project and install dependencies:

```bash
npm install
```

Start the application:

```bash
ng serve
```

The application will be available at:

```
http://localhost:4200
```

---

# Default Login Accounts

When data seeding is enabled, the following accounts are created automatically.

| Role          | Username  | Password       |
|---------------|-----------|----------------|
| Administrator | `admin`   | `ChangeMe123!` |
| Manager       | `manager` | `ChangeMe123!` |

---

# Verify the Installation

Once all services are running:

* Backend API: http://localhost:8080
* Domain Service: http://localhost:8081
* Angular UI: http://localhost:4200

The application should load with the seeded books and the default administrator and manager accounts ready for use.
