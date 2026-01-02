# TaskFlow - Task Approval System

A simple task approval system that demonstrates authentication, role-based authorization, and a basic workflow using Spring Boot.

## Features

### User Roles

- **USER**: Can create tasks, save tasks as draft, and submit tasks for approval
- **APPROVER**: Can view submitted tasks and approve or reject them

### Task Workflow

```
DRAFT → SUBMITTED → APPROVED
                → REJECTED
```

## Technology Stack

- **Backend**: Java 21, Spring Boot 4.0.2
- **Security**: Spring Security with JWT authentication
- **Database**: MySQL with JPA/Hibernate
- **Frontend**: Thymeleaf with Bootstrap 5
- **Build Tool**: Maven

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - User login
- `GET /api/auth/profile` - Get user profile

### Task Management (USER role)

- `POST /api/tasks` - Create a new task
- `POST /api/tasks/{id}/save-draft` - Save task as draft
- `PUT /api/tasks/{id}/submit` - Submit task for approval
- `GET /api/tasks/my` - Get current user's tasks

### Task Approval (APPROVER role)

- `GET /api/tasks/submitted` - Get submitted tasks
- `PUT /api/tasks/{id}/approve` - Approve a task
- `PUT /api/tasks/{id}/reject` - Reject a task

### General

- `GET /api/tasks/{id}` - Get task by ID
- `GET /api/tasks/status/{status}` - Get tasks by status

## Database Configuration

The system uses MySQL database. Update the following in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taskflow
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## Running the Application

1. Start MySQL database
2. Update database configuration in `application.properties`
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Access the application at `http://localhost:8080`

## Sample Usage

### Register Users

```bash
# Register a USER
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "role": "USER"
  }'

# Register an APPROVER
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "email": "jane@example.com",
    "password": "password123",
    "role": "APPROVER"
  }'
```

### Login and Create Tasks

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'

# Create task (use JWT token from login)
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Sample Task",
    "description": "This is a sample task for approval"
  }'
```

## Security Features

- JWT-based authentication
- Role-based access control
- Password encryption with BCrypt
- Stateless session management
- CORS and CSRF protection

## Project Structure

```
src/main/java/mo/demo/
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java
│   ├── TaskController.java
│   └── WebController.java
├── model/
│   ├── User.java
│   └── Task.java
├── repository/
│   ├── UserRepository.java
│   └── TaskRepository.java
├── security/
│   ├── JwtUtil.java
│   └── JwtAuthFilter.java
└── service/
    ├── CustomUserDetailsService.java
    └── TaskService.java
```

## Requirements Met

✅ User authentication and authorization  
✅ Role-based access control  
✅ Task creation and submission by USER  
✅ Task approval or rejection by APPROVER  
✅ Clean backend logic  
✅ State transitions management  
✅ Security concepts implementation

## Future Enhancements

- Email notifications for task approvals/rejections
- Task comments and attachments
- Advanced reporting and analytics
- Multi-tenant support
- Audit logging
