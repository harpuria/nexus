# Codex Agents Guide — NEXUS Project

## 1. Project Overview

**Project Name:** NEXUS

NEXUS is a backend-centric, multi-game operation platform designed for small to mid-scale casual and idle games.
The goal of this project is to provide reusable backend capabilities such as authentication, economy, products, coupons, mail, and admin operations — without rebuilding server infrastructure per game.

---

## 2. Tech Stack

- Java 21
- Spring Boot 3.5.0
- JOOQ 3.19.23
- PostgreSQL 17
- Gradle
- Lombok
- OpenAPI 3.x (Swagger UI)
- JWT-based authentication (User / Admin separated)

---

## 3. Core Design Principles
### 3.1 Backend-Centric Platform
- Business logic always lives in the backend
- No game-specific logic should be hardcoded unless explicitly required

### 3.2 Multi-Tenant First
- All domain data MUST be scoped by ORG_ID and/or GAME_ID
- Never assume a single organization or a single game context

### 3.3 Safety Over Convenience
When uncertain:
  - Prefer data integrity over performance shortcuts
  - Prefer explicit logic to implicit behavior
  - Prefer transactional safety to optimistic assumptions

---

## 4. Database Conventions
### 4.1 Common Columns
Most tables follow a shared base concept:
- `CREATED_AT`
- `CREATED_BY`
- `UPDATED_AT`
- `UPDATED_BY`
- `IS_DEL` (logical delete)

⚠️ Physical deletes are NOT allowed unless explicitly instructed.

### 4.2 Logical Delete Rules
- `IS_DEL = 'Y'` means inactive or removed
- Queries MUST explicitly filter `IS_DEL = 'N'`
  unless the use-case is audit or history

### 4.3 Transaction Rules
- All write operations MUST be transactional.
- Partial updates without rollback are NOT allowed.

---

## 5. Naming Convention
### 5.1 Variable and Class Naming Rules
- Use `camelCase` for variable names.
- Use `PascalCase` for class names.
- Use `UPPER_SNAKE_CASE` for constants.

### 5.2 Method Naming Rules
- Method names should generally follow the **action + domain name** format.
- The first letter of `[DOMAIN_NAME]` should be uppercase.  
  (e.g., `Admin`, `Admins`, `User`, `Order`)
- For methods beyond basic CRUD, use names that **clearly express the business intent**,
  with detailed naming subject to Codex judgment.

#### 5.2.1 Controller Class
- Controller methods use **verbs from the HTTP/REST perspective**.
- Do not use database-centric terms (`select`, `insert`, `update`, `delete`) in method names.

**CRUD Method Naming Guide**
- Create: `create[DOMAIN_NAME]`
- Single Retrieval: `get[DOMAIN_NAME]`
- List Retrieval: `list[DOMAIN_NAME]`
- Update: `update[DOMAIN_NAME]`
- Delete: `delete[DOMAIN_NAME]`

#### 5.2.2 Service Class
- Service methods must express **use cases and business actions**.
- Even simple pass-through methods must follow the same naming rules as Controllers.

**CRUD Method Naming Guide**
- Create: `create[DOMAIN_NAME]`
- Single Record Retrieval: `get[DOMAIN_NAME]`
- List Retrieval: `list[DOMAIN_NAME]`
- Update: `update[DOMAIN_NAME]`
- Delete: `delete[DOMAIN_NAME]`

**Additional Rules for Retrieval Methods**
- `get[DOMAIN_NAME]`
  - The target must exist.
  - Throws an exception if it does not exist.
- `find[DOMAIN_NAME]`
  - The target may not exist.
  - Allows returning `Optional` or `null`.

#### 5.2.3 Repository Class
- The Repository acts as the **persistence layer** and is solely responsible for database access.
- Query conditions must be clearly reflected in the method name.
- Del condition is MUST NOT be included in repository method names.  (e.g., use `findByGameId`, NOT `findByIsDelAndGameId`)
- Repository method names MUST reflect only the caller-provided conditions.
  Do NOT invent conditions such as `keyword`, `nameKeyword`, etc., unless they are explicitly required by the API/feature.

**CRUD Method Naming Guide**
- Create: `insert[DOMAIN_NAME]` (Repository only)
- Single query: `findBy[CONDITION]`
- List query: `findAllBy[CONDITION]`
- Existence check: `existsBy[CONDITION]`
- Update: `update[DOMAIN_NAME]` (Repository only)
- Delete: `delete[DOMAIN_NAME]`

**Examples**
- `findById`
- `findByEmail`
- `findAllByStatus`
- `existsByEmail`

#### 5.2.4 Request DTO Class
**Naming Rule**
`[Action][Domain]RequestDto` or `[Domain][Action]RequestDto`

**Examples**
- `CreateAdminRequestDto`
- `UpdateAdminRequestDto`
- `SearchAdminRequestDto`

#### 5.2.5 Response DTO Class
**Naming Convention**
- `[Domain]ResponseDto`

**Examples**
- `AdminResponseDto`
- `AdminDetailResponseDto`

#### 5.2.6 Entity Class
- Field composition follows the same format as jOOQ Pojo in `org.jooq.generated.tables.pojos`.

**Naming Convention**
- `[Domain]Entity`

**Example**
- `AdminEntity`

#### 5.2.7 Result Class
**Naming Convention**
- `[Domain]Result`

**Example**
- `AdminListResult`


#### 5.2.8 Etc Class
- Use names that clearly indicate the purpose for utility, helper, conversion, and similar classes.
- Detailed naming rules are determined by the Codex.

---

## 6. Code Style Guidelines
- Maintain clear separation of responsibilities between Controller / Service / Repository.
- Each layer must not expose the implementation details (e.g., DB, jOOQ, SQL) of lower layers.
- Method names should express **intent and role, not implementation details**.
- Details not specified in this document follow existing code style conventions.

### 6.1 Controller Class
- The path value in `@RequestMapping` uses constants defined in `ApiConstants.java`.
- If a constant does not exist, define it newly in `ApiConstants.java`.
- Use `ResponseEntity<ApiResponse<[TYPE]>>` as the return type.
- Use `@PatchMapping` as the default for update APIs.
- Avoid using `@PutMapping` unless replacing the entire resource.
- Use the `RequestDto` class for parameters passed when calling Services.
- Refer to `AdminController.java` for the default code style.

### 6.2 Service Class
- When paging is required,
  use `PagingUtil.getPagingEntity()` to create a paging entity.
- Transactions, validation, and permission checks are handled in the Service layer.
- Use `Result<[TYPE]>` as the return type.
- Parameters passed to repository calls use the `Entity` class or a single parameter (e.g., `int gameId`).
- `@Transactional` MUST be applied only to Service methods that define a transaction boundary.
- Do NOT apply `@Transactional` at the Service class level.
- Write operations SHOULD be transactional.
- Read-only operations SHOULD NOT be transactional unless consistency across multiple queries is required.
- Use `@Transactional(readOnly = true)` for read-only transaction scopes when needed.
- Refer to `AdminService.java` for the default code style.

### 6.3 Repository Class
- Queries MUST include the `IS_DEL = ‘N’` condition by default.
- `delete[DOMAIN_NAME]` methods MUST perform logical delete (`IS_DEL = ‘Y’`)
- Physical delete queries are NOT allowed unless explicitly instructed
- The return type should use the `Entity` class by default, but create and use a separate `Result` class if necessary.
- The method prototype for setting the sort field when querying a list is `private SortField<?> resolveSortField(String sort, String direction)`.
- Refer to `AdminRepository.java` for the default code style.

#### 6.3.1 Detailed Rules for Insert / Update Operations
**Common**
- All **INSERT** and **UPDATE** operations in the Repository layer **MUST be implemented using jOOQ `TableRecord`**.
- Writing INSERT/UPDATE logic by manually setting columns through the DSL API is **discouraged**.

**Timestamp**
- The `CREATED_AT` and `UPDATED_AT` columns are **automatically managed by a jOOQ Listener**.
- Therefore:
  - Repository layer **MUST NOT** set or modify `CREATED_AT` and `UPDATED_AT`
  - Service and Controller layers **MUST NOT** handle timestamp fields
- All timestamp values are considered **infrastructure concerns**, not business logic.

**Insert**
- Create a `TableRecord` instance via `dsl.newRecord(TABLE, entity)`
- Persist data using `record.store()`
- The method MUST return the **primary key (PK)** of the newly created row.

```java
// insert example (admin insert)
AdminRecord record = dslContext.newRecord(ADMIN, admin);
record.store();

return record.getAdminId();
```

**Update**
- Create a `TableRecord` instance via `dsl.newRecord(TABLE, entity)`
- marked as changed via `record.changed()` are eligible for update
- UPDATE operations should return the number of affected rows (int).
- An affected row count of 0 should be considered a failure at the Service layer.

```java
// update example (admin update)
AdminRecord record = dslContext.newRecord(ADMIN, admin);
record.changed(ADMIN.LOGIN_ID, admin.getLoginId() != null);
record.changed(ADMIN.LOGIN_PW, admin.getLoginPw() != null);
record.changed(ADMIN.ADMIN_ROLE, admin.getAdminRole() != null);
record.changed(ADMIN.ADMIN_NM, admin.getAdminNm() != null);
record.changed(ADMIN.ADMIN_EMAIL, admin.getAdminEmail() != null);
record.changed(ADMIN.ORG_ID, admin.getOrgId() != null);
record.changed(ADMIN.GAME_ID, admin.getGameId() != null);
record.changed(ADMIN.UPDATED_AT, admin.getUpdatedAt() != null);
record.changed(ADMIN.UPDATED_BY, admin.getUpdatedBy() != null);
record.changed(ADMIN.IS_DEL, admin.getIsDel() != null);
return record.update();
```

### 6.4 Request DTO Class
- Use the Lombok annotations `@Getter`, `@Setter`, and `@NoArgsConstructor`.
- Used to receive parameters in the Controller and pass them to the Service.
  - This project does not use Command/Condition objects for easier maintenance.
  - Used only up to the Service Layer; not used in subsequent layers.
- Use `javax/jakarta validation` annotations for input validation first.

### 6.5 Response DTO Class
- Use `@Getter` and `@Builder` Lombok annotations.
- Passes the Service's return result to the Controller, which ultimately returns it to the client.
- Contains only externally exposed fields. (Exposure of internal identifiers/state values follows policy)
- Date/time/number formatting follows project-wide conventions.

### 6.6 Entity Class
- Use `@Getter` and `@Builder` Lombok annotations.
- Objects created by the Service and passed to the Repository for use.

### 6.6 Result Class
- Use `@Getter` Lombok annotation.
- Object holding jOOQ query results.
- Entity and Result classes SHOULD be immutable where possible
- Avoid setters unless explicitly required

---

## 7. Priority Rule

- Rules in this document > Existing code style > General Codex rules
- If code conflicts with this document, modify it based on this document.

---

## 8. Code Comment Rules
### 8.1 No AI Attribution Comments
Do NOT add comments such as:
- “Modified by Codex”
- “Generated by AI”

Code ownership and history are tracked via Git.

### 8.2 Intent-Based Comments
Comments should explain:
- WHY the logic exists
- What assumptions it relies on
- What must NOT be changed casually

### 8.3 Recommended Comment Keywords
Use the following prefixes when appropriate:
- `WHY:`
- `DO NOT:`
- `ASSUMPTION:`
- `BOUNDARY:`

These keywords help both humans and AI
to preserve architectural intent.

---

## 9. Testing instructions