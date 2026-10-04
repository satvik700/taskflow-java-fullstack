# ✅ TaskFlow – Full-Stack Task Management App

A full-stack web application where users register, log in and manage their own tasks with priorities, due dates and status tracking.

## Tech Stack
- **Backend:** Java 17, Spring Boot 3, Spring Web (REST), Spring Data JPA / Hibernate, Bean Validation
- **Security:** BCrypt password hashing, token-based authentication, per-user data isolation
- **Database:** H2 (file-based, zero setup) – switch to MySQL by changing 3 lines in `application.properties`
- **Frontend:** HTML, CSS, JavaScript (Fetch API), served by Spring Boot
- **Build:** Maven

## Features
- User registration and login (hashed passwords, bearer-token auth)
- Create, read, update, delete tasks (full CRUD)
- Priority (LOW/MEDIUM/HIGH), due dates, status (TODO / IN_PROGRESS / DONE)
- Filter by status and live completion stats
- Users can only access their own tasks (403 on others)
- Input validation and clean JSON error responses

## REST API
| Method | Endpoint | Description |
|---|---|---|
| POST | /api/auth/register | Create account |
| POST | /api/auth/login | Login, returns token |
| GET | /api/tasks | List my tasks |
| POST | /api/tasks | Create task |
| PUT | /api/tasks/{id} | Update task |
| DELETE | /api/tasks/{id} | Delete task |

## Architecture
`Browser (HTML/JS)` → `Controller` → `Service` → `Repository (JPA)` → `H2/MySQL`

## Run Locally
Requirements: JDK 17+, Maven 3.8+
```bash
mvn spring-boot:run
```
Open http://localhost:8080

## Future Improvements
JWT with expiry, Spring Security roles, pagination, Docker, unit tests with JUnit/Mockito, deployment on Render/Railway.
