# AI Services Core

![Java](https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.13-6DB33F?logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white)
![Spring AI](https://img.shields.io/badge/Spring%20AI-1.1.4-6DB33F?logo=spring&logoColor=white)

Enterprise-ready backend service for secure AI chat workflows with local and social authentication, email verification, password recovery flows, role/permission-based authorization, token usage audit reporting, and Flyway-based schema management.

## Table of Contents

1. [Overview](#overview)
2. [Capabilities](#capabilities)
3. [Architecture](#architecture)
4. [Package Structure](#package-structure)
5. [Technology Stack](#technology-stack)
6. [Authentication and Security](#authentication-and-security)
7. [AI Runtime](#ai-runtime)
8. [API Reference](#api-reference)
9. [Data and Migrations](#data-and-migrations)
10. [Configuration](#configuration)
11. [Observability](#observability)
12. [Production Hardening Checklist](#production-hardening-checklist)
13. [Known Gaps and Roadmap](#known-gaps-and-roadmap)

## Overview

This service is a Spring Boot 3 application that exposes AI chat and identity APIs under `/api`.

It supports:

- Local auth (`email + password`) and OAuth2 (Google, GitHub)
- Email verification and verification re-send
- Forgot/reset password and authenticated change-password flow
- Permission-gated chat modes (`generic`, `aviation`)
- User profile and preference management
- Admin endpoints for user/permission management and AI token usage analytics
- Profile-based runtime (`dev` with H2, `prod` with PostgreSQL)
- Database schema management via Flyway

## Capabilities

### Identity and Access

- JWT access tokens with purpose-based claims
- Email verification token flow
- Password reset token flow with token reuse protection
- Account linking behavior for OAuth2 users based on provider/email resolution
- Role and permission model (`ROLE_USER`, `ROLE_ADMIN`, granular permissions)

### AI Chat

- Multi-provider architecture with chat-type registry
- Per-chat-type memory windows and isolated conversations
- Aviation mode with domain tool-calling support
- Token usage auditing (prompt/completion/total tokens, model, latency, cost)

### User Management

- Self-service profile management via `/api/me`
- User preference management
- Admin user management and role/permission assignment

### Operations

- Global API response wrapper for consistent contracts
- Centralized exception mapping
- Actuator endpoints for runtime health and diagnostics
- Docker multi-stage build
- Email notifications via Brevo integration

## Architecture

```mermaid
flowchart TD
  Client["Web or API Client"]
  
  API["REST API Layer<br/>auth, chat, profile, admin, oauth2"]
  Security["Spring Security<br/>JWT Filter + Permission Evaluator"]
  
  AuthSvc["Auth Service<br/>Local + OAuth2"]
  ChatSvc["Chat Service<br/>Generic, Aviation"]
  ProfileSvc["Profile Service"]
  AdminSvc["Admin Service"]
  EmailSvc["Email Service<br/>Brevo Integration"]
  
  ChatProviders["Chat Providers<br/>Generic, Aviation with Tools"]
  TokenAudit["Token Usage Advisor<br/>Async Audit"]
  
  Repos["JPA Repositories"]
  Migrations["Flyway Migrations"]
  
  OAuth["OAuth2 Providers<br/>Google, GitHub"]
  AIModels["AI Models<br/>via Spring AI"]
  
  DB["Database<br/>H2 / PostgreSQL"]
  
  Client --> API
  API --> Security
  Security --> AuthSvc
  Security --> ChatSvc
  Security --> ProfileSvc
  Security --> AdminSvc
  
  AuthSvc --> OAuth
  AuthSvc --> EmailSvc
  
  ChatSvc --> ChatProviders
  ChatProviders --> AIModels
  ChatSvc --> TokenAudit
  
  EmailSvc -.-> Brevo["Brevo Email<br/>Service"]
  
  AuthSvc --> Repos
  ChatSvc --> Repos
  ProfileSvc --> Repos
  AdminSvc --> Repos
  TokenAudit --> Repos
  
  Repos --> DB
  Migrations --> DB
```

## Package Structure

Domain-driven package organization with clear separation of concerns:

```
com/arsan/ai/
├── core/                          # Framework & Infrastructure
│   ├── config/                    # Spring beans configuration
│   ├── security/
│   │   ├── filter/               # JWT authentication filter
│   │   ├── handler/              # Success & Error handlers
│   │   ├── service/              # JWT & security services
│   │   ├── evaluator/            # Permission evaluator
│   │   └── constants/            # Security constants
│   ├── exception/                # Exception handling
│   ├── advice/                   # Global advice
│   ├── annotation/               # Custom annotations
│   └── properties/               # Configuration properties
├── shared/                        # Shared Domain Layer
│   ├── entity/                  # Shared entities
│   ├── repository/              # Shared repositories
|   |    └── projection           # Database read projections
│   ├── mapper/                  # Entity-DTO mappers
│   ├── model/                   # Shared DTOs & value objects
│   └── util/                    # Cross-cutting utilities
├── auth/                         # Authentication Domain
│   ├── controller/              # /api/auth endpoints
│   ├── service/                 # Authentication logic
│   ├── provider/                # OAuth2 providers
│   ├── resolver/                # OAuth identity resolvers
│   ├── model/                   # Auth DTOs & requests
│   ├── enums/                   # Auth-specific enums
│   └── events/                  # Auth domain events
├── profile/                       # User Profile Domain
│   ├── controller/              # Profile endpoints
│   ├── model/                   # Profile DTOs
│   └── service/                 # User business logic
├── chat/                         # AI Chat Domain
│   ├── advisor/                 # Usage auditing advisor
│   ├── controller/              # /api/ai/chat endpoints
│   ├── service/                 # Chat orchestration
│   ├── provider/                # Chat provider implementations
│   ├── model/                   # Chat DTOs
│   ├── enums/                   # Chat-specific enums
│   ├── tool/                    # Tool calling (FuelServiceTool, etc.)
│   └── util/                    # Chat utilities
├── admin/                        # Admin Management Domain
│   ├── controller/              # Admin endpoints
│   ├── service/                 # Admin business logic
│   ├── entity/                  # Admin entities
│   ├── repository/              # Admin repositories
|   |   └── projection           # Database read projections 
│   └── model/                   # Admin DTOs
├── notification/                 # Notification Domain
│   └── email/                   # Email service
│       ├── constants/           # Email constants
│       ├── listener/            # Event listeners
│       ├── model/               # Email DTOs
│       └── service/             # Email business logic
└── SpringAiApplication.java     # Main application class
```

### Package Responsibilities

| Package | Responsibility |
|---------|-----------------|
| **core/security/** | Spring Security framework setup, JWT token operations, permission evaluation |
| **core/config/** | Spring bean configuration (AI clients, async executors, etc.) |
| **core/exception/** | Global exception handling and normalization |
| **core/advice/** | Global exception advice and request/response wrapping |
| **shared/** | Shared domain models, repositories, utilities, and DTOs used across domains |
| **auth/** | Authentication workflows (login, register, OAuth2, email verification) |
| **profile/** | User profile management (self-service endpoints) |
| **chat/** | AI chat provider integration and conversation management |
| **admin/** | Administrative functions (user management, token usage audit reporting) |
| **notification/** | Email and other notification services |

## Technology Stack

- Java 21
- Spring Boot 3.5.13
- Spring Security (JWT + OAuth2 Client + method security)
- Spring Data JPA + Hibernate
- Spring AI 1.1.4
- Flyway
- H2 (dev profile)
- PostgreSQL (prod profile)
- SpringDoc OpenAPI / Swagger UI (dev profile)
- Spring Boot Actuator
- Maven
- Docker

## Authentication and Security

### Authentication Modes

- Local login and registration
- OAuth2 social login (`google`, `github`)

### JWT Purposes

Tokens carry a `purpose` claim and are validated by purpose:

- `ACCESS`
- `EMAIL_VERIFICATION`
- `PASSWORD_RESET`

Configured expirations (minutes):

- `access-expiration-in-minutes` (default 120)
- `email-verification-expiration-in-minutes` (default 30)
- `password-reset-expiration-in-minutes` (default 10)

### Verification and Password Flows

- **Register** creates user, sends verification email, and returns access token
- **Verify email** marks user as verified and stores `verifiedDate`
- **Resend verification** supports non-verified users
- **Forgot password** sends reset email for local accounts
- **Reset password** validates token purpose/expiry and blocks token reuse if already reset
- **Change password** requires authenticated user and validates current/new password rules

### Authorization

Security is enabled by default (`app.security.enabled=true`).

Protected endpoints use:

- Request matcher rules for admin APIs
- Method-level check for chat permission per `chatType`

Permissions currently include:

- Admin: `admin:read`, `admin:write`, `admin:delete`
- User: `user:read`, `user:write`, `user:delete`, `user:manage`
- Audit: `token:usage:read`
- Chat: `chat:generic:use`, `chat:aviation:use`

### CORS

CORS origins are externalized (`app.cors.allowed-origins`) and currently include local and deployed frontend origins.

## AI Runtime

### Chat Types

- `generic`
- `aviation`

### Chat Client Wiring

- Generic chat client:
  - Message window memory (max 10)
  - Token usage advisor enabled
- Aviation chat client:
  - System prompt from `src/main/resources/prompts/aviation-prompt.st`
  - Message window memory (max 10)
  - Token usage advisor enabled
  - `FuelServiceTool` registered for tool calls

### Conversation Scope

Conversation ID is resolved per authenticated user ID and can be cleared by chat type via API.

### Usage Auditing

Every AI call attempts asynchronous persistence of:

- User
- Model/provider
- Prompt/completion/total token counts
- Estimated cost
- Latency
- Input/output summaries

## API Reference

Base path: `/api`

### Auth

- `POST /auth/login`
- `POST /auth/register`
- `GET /auth/availability?email=...`
- `POST /auth/verify-email`
- `POST /auth/resend-verification`
- `POST /auth/forgot-password`
- `POST /auth/reset-password`

### OAuth2

- `GET /oauth2/providers`
- `GET /oauth2/{providerType}`

OAuth2 success/failure redirects go to frontend paths:

- `/oauth-success?token=...`
- `/oauth-error?message=...`

### Current User (Self-Service)

- `GET /me` → Profile
- `POST /me/change-password` → Change password

### AI Chat

- `GET /ai/chat/types`
- `POST /ai/chat/{chatType}`
- `DELETE /ai/chat/{chatType}/conversation`

Example request body:

```json
{
  "message": "Summarize the latest fuel discrepancy report."
}
```

### Admin: Users

- `GET /admin/users`
- `GET /admin/users/{id}`
- `PATCH /admin/users/role/grant/{id}`
- `PATCH /admin/users/role/revoke/{id}`
- `PATCH /admin/users/permission/grant/{id}`
- `PATCH /admin/users/permission/revoke/{id}`
- `GET /admin/users/permission/available`

### Admin: Token Usage

- `GET /admin/token-usage?page=0&size=20`
- `GET /admin/token-usage/user/{userId}`
- `GET /admin/token-usage/date-range?startDate=...&endDate=...`
- `GET /admin/token-usage/total-tokens?startDate=...&endDate=...`
- `GET /admin/token-usage/total-tokens/user/{userId}?startDate=...&endDate=...`
- `GET /admin/token-usage/summary?startDate=...&endDate=...`

Date query parameters use ISO datetime format.

### API Response Contract

Responses are wrapped in:

```json
{
  "success": true,
  "message": "Success",
  "data": {},
  "error": null
}
```

Exceptions are normalized through global exception handling for validation, auth, authorization, JWT, AI provider, persistence, and fallback server errors.

## Data and Migrations

### Core Tables

- `app_user`
- `app_user_roles`
- `app_user_permissions`
- `token_usage_audit`

### Flyway

- Flyway is enabled in both `dev` and `prod`
- Migration scripts are under `src/main/resources/db/migration`
- Current schema baseline: `V1__init_schema.sql`
- Seed data script exists, but its is not wired for data insertion.

## Configuration

### Profiles

- `dev`
  - H2 file database
  - `ddl-auto=validate`
  - Flyway enabled, migration locations set to `classpath:db/migration,classpath:db/dev`
  - H2 console enabled at `/api/h2-console`
  - Swagger/OpenAPI enabled
- `prod`
  - PostgreSQL datasource from environment variables
  - `ddl-auto=validate`
  - Flyway enabled
  - Swagger/OpenAPI disabled

### Environment Variables

Required for core runtime:

- `OPENAI_API_KEY`
- `JWT_SECRET_KEY`
- `MAIL_USERNAME`
- `MAIL_PASSWORD`
- `BREVO_API_KEY`
- `BREVO_SENDER_EMAIL`
- `GOOGLE_CLIENT_ID`
- `GOOGLE_CLIENT_SECRET`
- `GITHUB_CLIENT_ID`
- `GITHUB_CLIENT_SECRET`

Required for production datasource:

- `DATABASE_URL`
- `DATABASE_USERNAME`
- `DATABASE_PASSWORD`

Optional / operational:

- `PORT` (default `8080`)

Mandatory for bootstrap admin creation:

- `ADMIN_EMAIL`
- `ADMIN_PASSWORD`

### Bootstrap Admin

When `app.bootstrap.admin.enabled=true`, both `ADMIN_EMAIL` and `ADMIN_PASSWORD` are required to create the admin user at startup (if the user does not already exist).

## Observability

Actuator endpoints exposed via `/api/actuator` include:

- `health`
- `info`
- `metrics`
- `env`
- `loggers`
- `threaddump`

Swagger/OpenAPI in dev:

- `/api/v3/api-docs`
- `/api/swagger-ui.html`

## Production Hardening Checklist

- Use a strong managed JWT secret (prefer Base64-encoded key material)
- Restrict CORS origins to trusted frontend domains only
- Consider refresh-token strategy and token revocation/blacklisting
- Add rate limiting for auth and chat endpoints
- Replace in-memory chat memory for horizontal scale
- Reduce exposed actuator endpoints in internet-facing environments
- Enforce SMTP credentials and sender reputation controls
- Add API integration and security regression test coverage

## Known Gaps and Roadmap

- Aviation tool methods currently return mocked data
- **Externalize config with refresh scope** - Integrate Spring Cloud Config Server for centralized configuration management with `@RefreshScope` support, enabling dynamic property updates without application restart (CORS origins, feature flags, AI model parameters, etc.)
- **Add redis** - Implement Redis caching layer for session storage, conversation memory, user preferences, and token blacklisting to reduce database load and improve response times for frequently accessed data
- **Add rate limiter** - Implement API rate limiting using Resilience4j or Spring Cloud Gateway to protect endpoints from abuse, with per-user and per-endpoint limits for auth, chat, and admin APIs
- **Add kafka for notification system** - Integrate Apache Kafka for asynchronous event-driven architecture to handle user notifications (token usage alerts, password reset confirmation, new feature announcements), decoupling notification producers from consumers for better scalability
- User Preferences/Settings - Store user preferences (model selection, temperature settings, etc.)
- Test coverage does not yet include full auth/chat/admin integration paths
