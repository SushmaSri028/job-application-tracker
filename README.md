# Job Application Tracker — Backend

Full-stack job application tracker for software engineering job seekers. This is the **Spring Boot 3 REST API**.

🔗 **Live demo:** https://job-tracker-ui.vercel.app
📦 **Frontend repo:** [SushmaSri028/job-tracker-ui](https://github.com/SushmaSri028/job-tracker-ui)

> ⚠️ Backend hosted on Render's free tier — it sleeps after 15min of inactivity. First request after idle takes ~30s to wake up.

---

## Tech Stack

- **Java 21** + **Spring Boot 4.1**
- **PostgreSQL 18** with JPA / Hibernate ORM
- **Spring Security** with **JWT** (HS384 signed tokens)
- **BCrypt** password hashing
- **JUnit 5** + **Mockito** for testing
- **Docker** (multi-stage build) — deployed to **Render**

---

## Features

- 🔐 User registration + JWT-based login
- 🔒 Per-user data isolation (each user only sees their own applications)
- 📝 Full CRUD for job applications (POST/GET/PUT/DELETE)
- 🎯 Status workflow (Applied → Screening → Interview → Offer → Accepted/Rejected/Ghosted/Declined)
- ✅ Field validation with `@Valid`
- 🚨 Centralized exception handling — proper 404/403/400/409 status codes
- 🌐 CORS configured for cross-origin frontend access
- ✅ Unit tested service layer (~90% coverage)

---

## Architecture

\`\`\`
┌─────────────────────┐
│   React (Vercel)    │
└──────────┬──────────┘
│ HTTPS + JWT
▼
┌─────────────────────┐    ┌─────────────────────┐
│ Spring Boot (Render)│───►│ PostgreSQL (Render) │
│   + JWT Filter      │    │   users +           │
│   + Spring Security │    │   applications      │
└─────────────────────┘    └─────────────────────┘
\`\`\`

Layered architecture: **Controller → Service → Repository → Entity → DTO**

---

## API Endpoints

| Method | Path | Auth | Description |
|---|---|---|---|
| GET | `/api/health` | No | Health check |
| POST | `/api/auth/register` | No | Create new user, returns JWT |
| POST | `/api/auth/login` | No | Login, returns JWT |
| GET | `/api/applications` | Yes | List my applications |
| POST | `/api/applications` | Yes | Create application |
| GET | `/api/applications/{id}` | Yes | Get one |
| PUT | `/api/applications/{id}` | Yes | Update |
| DELETE | `/api/applications/{id}` | Yes | Delete |

Auth: send `Authorization: Bearer <jwt-token>` header on protected routes.

---

## Local Setup

### Prerequisites
- Java 21
- Maven 3.9+
- PostgreSQL 16+

### Steps

\`\`\`bash
# 1. Clone
git clone git@github.com:SushmaSri028/job-application-tracker.git
cd job-application-tracker

# 2. Create database
psql postgres -c "CREATE DATABASE jobtracker;"
psql postgres -c "CREATE USER jobtracker_user WITH PASSWORD 'devpassword';"
psql postgres -c "GRANT ALL PRIVILEGES ON DATABASE jobtracker TO jobtracker_user;"

# 3. Run
./mvnw spring-boot:run
\`\`\`

Server starts on `http://localhost:8080`.

### Run tests

\`\`\`bash
./mvnw test
\`\`\`

---

## Deployment

Containerized with a multi-stage Dockerfile. Deployed via Render's Docker runtime with GitHub auto-deploy.

Secrets (JWT signing key, DB credentials) injected as environment variables — never committed.

---

## Roadmap

- [ ] Flyway migrations (currently using Hibernate `ddl-auto=update` due to Spring Boot 4-RC quirks)
- [ ] Refresh tokens
- [ ] OAuth2 (Google sign-in)
- [ ] Email reminders for stale applications
- [ ] Bulk import from CSV
- [ ] Resume PDF attachment with version tracking

---

## About

Built by **Sushma Sri Kondamareddy** as a portfolio project during my MS CS at Florida Atlantic University (graduating May 2026).

Open to Java Full Stack / Application Engineering roles starting June 2026 🇺🇸 (OPT/F-1, STEM extension eligible).

📧 kondamareddysushmasri03@gmail.com
🔗 [LinkedIn](https://linkedin.com/in/your-handle)