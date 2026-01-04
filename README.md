# TaskFlow - Task Approval System

A modern task approval system with a React frontend and Spring Boot backend, demonstrating authentication, role-based authorization, and workflow management.

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

- **Frontend**:
  - React 19 with TypeScript
  - Vite for build tooling
  - TailwindCSS for styling
  - Shadcn Ui
  - React Router for client-side routing
  - Axios for API requests
    
- **Backend**:
  - Java 21
  - Spring Boot 3.2.0
  - Spring Security with JWT authentication
  - Spring Data JPA with Hibernate
  - Maven for dependency management
 
- **Database**:
  - MySQL (hosted on Aiven)
  - Flyway for database migrations

- **Development Tools**:
  - Node.js 24 for frontend development
  - Java 21 for backend development

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user

  ```json
  {
    "name": "string",
    "email": "string",
    "password": "string",
    "role": "APPROVER" //Default "USER"
  }
  ```

- `POST /api/auth/login` - User login

  ```json
  {
    "email": "string",
    "password": "string"
  }
  ```

- `GET /api/auth/profile` - Get current user profile

### Task Management

- `GET /api/tasks/all` - Get all tasks (requires authentication)
- `POST /api/tasks/create` - Create a new task
- `GET /api/tasks/{id}` - Get task by ID
- `PUT /api/tasks/{id}/submit` - Submit task for approval (USER role)
- `PUT /api/tasks/{id}/approve` - Approve a task (APPROVER role)
- `PUT /api/tasks/{id}/reject` - Reject a task (APPROVER role)
- `GET /api/tasks/submitted` - Submitted tasks only (APPROVER role)

## Database Configuration

The application is pre-configured to use a MySQL database hosted on Aiven. The connection details are set in `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://authorization-demo-mysql-os96.l.aivencloud.com:17470/mo_demo?ssl-mode=REQUIRED
    username: avnadmin
    password: AVNS_y7MS8LI4ZQn1F8nnyFK
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

For local development, you can create a local MySQL database and update these values accordingly.

## Prerequisites

- Java 21 JDK
- Node.js 18+ and npm
- MySQL client (optional, if not using the provided Aiven database)

## Running the Application

### Backend

1. Navigate to the project root directory
2. Start the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```
   The backend will be available at `http://localhost:8080`

### Frontend

1. Open a new terminal
2. Navigate to the frontend directory:
   ```bash
   cd mo-demo-frontend
   ```
3. Install dependencies:
   ```bash
   npm install
   ```
4. Start the development server:
   ```bash
   npm run dev
   ```
   The frontend will be available at `http://localhost:5173/login`

## Environment Variables

The application uses the following environment variables:

### Backend

- `SPRING_DATASOURCE_URL`: JDBC URL for the database
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `JWT_SECRET`: Secret key for JWT token generation

### Frontend

- `VITE_API_URL`: Base URL for API requests (default: `http://localhost:8080`)

## Sample Usage

### 1. Register a New User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "isApprover": false
  }'
```

### 2. Login and Get JWT Token

```bash
# Login and save JWT token
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@example.com","password":"password123"}' \
  | jq -r '.token')

echo "Your JWT token: $TOKEN"
```

### 3. Create a New Task

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Complete Project Documentation",
    "description": "Update all project documentation to reflect recent changes"
  }'
```

### 4. Submit Task for Approval

```bash
curl -X PUT http://localhost:8080/api/tasks/1/submit \
  -H "Authorization: Bearer $TOKEN"
```

### 5. Approve/Reject Task (Approver Only)

```bash
# Approve
curl -X PUT http://localhost:8080/api/tasks/1/approve \
  -H "Authorization: Bearer $TOKEN"

# Reject
curl -X PUT http://localhost:8080/api/tasks/1/reject \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"reason": "Incomplete information"}'
```

## Security Features

- **JWT-based Authentication**: Secure token-based authentication
- **Role-based Access Control**: Fine-grained permissions for different user roles
- **Password Hashing**: BCrypt password encoding
- **HTTPS**: Secure communication with the database (Aiven MySQL with SSL)
- **CORS**: Configured for frontend development
- **Input Validation**: Server-side validation of all inputs
- **Secure Headers**: Added security headers for web application protection

## Project Structure

```
Mo_Demo/
├── mo-demo-frontend/          # React frontend
│   ├── public/                # Static files
│   ├── src/                   # Source files
│   │   ├── assets/            # Static assets
│   │   ├── components/        # React components
│   │   ├── pages/             # Page components
│   │   ├── services/          # API services
│   │   ├── App.tsx            # Main App component
│   │   └── main.tsx           # Entry point
│   └── package.json
│
├── src/                       # Backend source
│   ├── main/java/mo/demo/
│   │   ├── config/           # Configuration classes
│   │   ├── controller/       # REST controllers
│   │   ├── model/            # JPA entities
│   │   ├── repository/       # Data repositories
│   │   ├── security/         # Security configuration
│   │   └── service/          # Business logic
│   └── main/resources/       # Resources
│       └── application.yml   # Application configuration
│
├── .gitignore
├── pom.xml                  # Maven configuration
└── README.md
```

## Requirements Met

✅ User authentication and authorization  
✅ Role-based access control  
✅ Task creation and submission by USER  
✅ Task approval or rejection by APPROVER  
✅ Clean backend logic  
✅ State transitions management  
✅ Security concepts implementation

## Development

### Frontend Development

```bash
cd mo-demo-frontend
npm install
npm run dev
```

### Backend Development

```bash
# Run with Maven wrapper
./mvnw spring-boot:run

# Run tests
./mvnw test
```

### Building for Production

```bash
# Build frontend
cd mo-demo-frontend
npm run build

# Build backend
cd ..
./mvnw clean package -DskipTests
```

#### The API tests are included in a Postman collection.The Postman collection already contains test scripts.
