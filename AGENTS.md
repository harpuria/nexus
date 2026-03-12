# Codex Agents Guide — NEXUS Project

## 1. NEXUS 프로젝트 개요 (Project Overview - NEXUS)
본 프로젝트는 중소규모 개발사, 인디게임팀 등의 캐주얼 게임의 서버 지원을 위해 설계된 백엔드 중심의 멀티 게임 운영 플랫폼이다. 본 프로젝트의 목표는 게임마다 서버 인프라를 재구축하지 않고도 인증, 아이템, 상점, 쿠폰 등 재사용 가능한 백엔드 기능을 제공하는 것이다.

---

## 2. 기술 스택 (Tech Stack)
- Java 21
- Spring Boot 3.5.0
- JOOQ 3.19.23
- PostgreSQL 17
- Gradle
- Lombok
- OpenAPI 3.x (Swagger UI)
- JWT-based authentication (User / Admin separated)

---

## 3. Codex 코드 작성 기본 규칙
- 성능 단축보다 데이터 무결성을 우선시한다.
- 암묵적 동작보다 명시적 로직을 우선시한다.
- 낙관적 가정보다 트랜잭션 안전성을 우선시한다.
- **AGENTS.md 문서에 있는 내용을 최우선**으로 하여 코드를 작성한다.
- AGNETS.md 문서를 참조하기 어렵다면 **기존 코드 스타일을 참조**하며, 기존 코드 스타일 역시 참조하기 어려운 경우 **Codex의 일반적인 스타일**로 코드를 작성한다.
  - **AGENTS.md > 기존 코드 스타일 > Codex 일반 스타일**
- Git PR 내용 작성시 별도의 지시가 없는 한 **한국어(Korean)로 작성**한다.

---

## 4. 명명 규칙 (Naming Convention)
### 4.1 변수 및 클래스 명명 규칙
- 변수명은 `camelCase`를 사용.
- 클래스명은 `PascalCase`를 사용.
- 상수명은 `UPPER_SNAKE_CASE`를 사용.

### 4.2 메서드 명명 규칙
- 메서드명은 일반적으로 **동작명 + 도메인명** 형식을 따른다.
- `[DOMAIN_NAME]`의 첫 글자는 대문자로 표기한다.  
  (예: `Admin`, `Admins`, `User`, `Order`)
- 기본 CRUD외의 메서드의 경우, **비즈니스 의도를 명확히 표현하는** 이름을 사용하며, 상세한 명명 규칙은 코덱스 판단에 따른다.

#### 4.2.1 Controller Class
- 컨트롤러 메서드는 **HTTP/REST 관점의 동사**를 사용한다.
- 메서드명에 데이터베이스 중심 용어(`select`, `insert`, `update`, `delete`)를 사용하지 않는다.
- CRUD 메서드 명명 가이드
  - 생성: `create[DOMAIN_NAME]`
  - 단일 조회: `get[DOMAIN_NAME]`
  - 목록 조회: `list[DOMAIN_NAME]`
  - 업데이트: `update[DOMAIN_NAME]`
  - 삭제: `delete[DOMAIN_NAME]`

#### 4.2.2 Service Class
- 서비스 메서드는 **사용 사례와 비즈니스 동작**을 표현해야 한다.
- 단순 전달 메서드도 컨트롤러와 동일한 명명 규칙을 따라야 한다.
- CRUD 메서드 명명 가이드
  - 생성: `create[DOMAIN_NAME]`
  - 단일 레코드 조회: `get[DOMAIN_NAME]` or `find[DOMAIN_NAME]`
    - `get[DOMAIN_NAME]`은 대상이 존재해야함. (존재하지 않으면 예외 발생)
    - `find[DOMAIN_NAME]`은 대상이 존재하지 않을 수 있음. (`Optional` 또는 `null` 반환 허용)
  - 목록 조회: `list[DOMAIN_NAME]`
  - 업데이트: `update[DOMAIN_NAME]`
  - 삭제: `delete[DOMAIN_NAME]`

#### 4.2.3 Repository Class
- 리포지토리는 **영속성 계층** 역할을 하며 데이터베이스 접근을 전적으로 담당한다.
- 쿼리 조건은 메서드 이름에 명확히 반영되어야 한다.
- 삭제 조건은 리포지토리 메서드 이름에 포함되어서는 안 된다. (ex: `findByGameId` 사용, `findByIsDelAndGameId` 사용 금지)
- 리포지토리 메서드 이름은 호출자가 제공한 조건만을 반영해야 한다.
  API/기능에서 명시적으로 요구하지 않는 한 `keyword`, `nameKeyword` 등과 같은 조건을 임의로 생성해서는 안 된다.
- CRUD 메서드 명명 가이드
  - 생성: `insert[DOMAIN_NAME]`
  - 단일 쿼리: `findBy[CONDITION]`
  - 목록 쿼리: `findAllBy[CONDITION]`
  - 존재 확인: `existsBy[CONDITION]`
  - 업데이트: `update[DOMAIN_NAME]`
  - 삭제: `delete[DOMAIN_NAME]`

#### 5.2.4 Request DTO Class
- 명명 가이드
  - `[ACTION][DOMAIN_NAME]RequestDto` or `[DOMAIN_NAME][ACTION]RequestDto`
    - ex) `CreateAdminRequestDto`,`UpdateAdminRequestDto`,`SearchAdminRequestDto`

#### 5.2.5 Response DTO Class
- 명명 가이드
  - `[DOMAIN_NAME]ResponseDto`
    - ex) `AdminResponseDto`,`AdminDetailResponseDto`

#### 5.2.6 Entity Class
- 기본적으로 필드 구성은 `org.jooq.generated.tables.pojos` 패키지내 jOOQ Pojo와 동일한 형식을 따른다.
- 명명 가이드
  - `[DOMAIN_NAME]Entity`
    - ex) `AdminEntity`

#### 5.2.7 Result Class
- 명명 가이드
  - `[DOMAIN_NAME]Result`
    - ex) `AdminListResult`

#### 5.2.8 Etc Class
- Utility, Helper, Conversion 등 유사 클래스의 목적을 명확히 나타내는 이름을 사용한다.
- 세부 명명 규칙은 Codex에서 정한다.

---
## 6. 코드 스타일 가이드라인 (Code Style Guidelines)
- Controller, Service, Repository간에 명확한 역할 분리를 유지한다.
- 각 계층은 하위 계층의 구현 세부사항(예: DB, jOOQ, SQL)을 노출해서는 안 된다.
- 본 문서에 명시되지 않은 세부사항은 기존 코드 스타일 규칙을 따릅니다.
- **별도로 명시되지 않는 한, Codex 내에서 임의로 새 메서드를 생성하는 것은 금지한다.**
- 비즈니스 로직은 항상 백엔드에 존재해야하며, 명시적으로 요구되지 않는 한 하드코딩 절대 금지한다.

### 6.1 Controller Class
- `@RequestMapping`의 경로 값은 `ApiConstants.java`에 정의된 상수 사용하며, 상수가 없는 경우 새로 정의한다.
- 반환 타입은 `ResponseEntity<ApiResponse<[TYPE]>>`를 사용한다.
- Update API는 기본적으로 `@PatchMapping`을 사용하며, `@PutMapping` 사용은 리소스 전체 갱신이 필요한 경우에 한하여 사용한다.
- Service method 호출 시 전달되는 매개변수에는 `RequestDto` 클래스를 사용한다.
- 기본 코드 스타일은 `AdminController.java`를 참조한다.

### 6.2 Service Class
- 페이징이 필요한 경우,`PagingUtil.getPagingEntity()`를 사용하여 페이징 엔티티를 생성한다.
- 트랜잭션, 유효성 검사 및 권한 확인은 서비스 계층에서 처리된다.
- 반환 타입은 `Result<[TYPE]>`를 사용한다.
- 리포지토리 호출에 전달되는 매개변수는 `Entity` 클래스 또는 단일 매개변수(예: `int gameId`)를 사용한다.
- `@Transactional`은 트랜잭션 경계를 정의하는 서비스 메서드에만 적용해야한다.
- 서비스 클래스 수준에는 `@Transactional`을 적용하지 않는다.
- 모든 쓰기 작업은 반드시 트랜잭션 방식으로 수행되어야 한다.
- 롤백 없이 부분 업데이트를 수행하는 것은 허용되지 않는다.
- 다중 쿼리 간 일관성이 요구되지 않는 한 읽기 전용 작업은 트랜잭션 처리되지 않아야 한다.
- 필요한 경우 읽기 전용 트랜잭션 범위에 `@Transactional(readOnly = true)`를 사용한다.
- 모든 아이템(Item)의 보상(Rewards)지급 처리는 RewardService 에서 담당한다.
```java
// 우편 보상 지급 처리 예시 코드
GrantDto grantDto = GrantDto.builder()
        .gameId(userMailEntity.getGameId())
        .sourceId(dto.getUserMailId().toString())
        .rewards(CommonUtil.jsonbToDto(userMailEntity.getRewards(), RewardDto.class))
        .sourceType(SourceType.MAIL)
        .build();

boolean grantRst = rewardService.grant(grantDto);

if(!grantRst)
    return Result.Failure.of("우편 보상 지급 처리 실패.", ErrorCode.INTERNAL_ERROR.getCode());
```
- 기본 코드 스타일은 `AdminService.java`를 참조한다.

### 6.3 Repository Class
- Select query는 기본적으로 `IS_DEL = ‘N’` 조건을 포함해야 한다.
- `delete[DOMAIN_NAME]` 메서드는 논리적 삭제(`IS_DEL = ‘Y’`)를 수행해야 한다.
  - 명시적으로 지시하지 않는 한 **물리적 삭제는 절대 허용하지 않는다**.
- 반환 타입은 기본적으로 `Entity` 클래스를 사용해야 하지만, 필요한 경우 별도의 `Result` 클래스를 생성하여 사용하십시오.
- 목록 쿼리 시 정렬 필드를 설정하는 메서드 프로토타입은 `private SortField<?> resolveSortField(String sort, String direction)`이다.
- 기본 코드 스타일은 `AdminRepository.java`를 참조한다.
- 리포지토리의 기본 필드는 `DSLContext`, `J[DOMAIN]`, `[DOMAIN]Dao` 객체를 생성한다. DAO가 즉시 사용되지 않더라도 이들은 사전 구성된다.
```java
// Repository Class의 기본 필드 및 생성자 예시 (ADMIN)
private final DSLContext dslContext;
private final JAdmin ADMIN = JAdmin.ADMIN;
private final AdminDao dao;

public AdminRepository(Configuration configuration, DSLContext dslContext) {
    this.dslContext = dslContext;
    this.dao = new AdminDao(configuration);
}
```

#### 6.3.1 INSERT / UPDATE 작업에 대한 세부 규칙
- `CREATED_AT` 및 `UPDATED_AT`열은 **jOOQ 리스너에 의해 자동 관리되기 때문에**, 별도로 설정하거나 수정하지 않는다.
- INSERT (삽입)
  - `dsl.newRecord(TABLE, entity)`를 통해 `TableRecord` 인스턴스 생성하고, `record.store()`를 사용하여 데이터 저장한다.
  - INSERT 메서드는 새로 생성된 행의 **기본 키(PK)를 반드시 반환**한다.
```java
// insert example (admin insert)
AdminRecord record = dslContext.newRecord(ADMIN, admin);
record.store();

return record.getAdminId();
```
- UPDATE (수정)
  - `dsl.newRecord(TABLE, entity)`를 통해 `TableRecord` 인스턴스 생성하고, `record.changed()`로 변경 표시된 레코드만 UPDATE 가능하다.
    - 단, 복잡하거나 다양한 조건식이 필요한 경우 DSL API를 통한 방식으로도 작성이 가능하다.
  - UPDATE 메서드는 영향을 받은 **행 수(정수)를 반드시 반환**한다.
    - Service layer에서는 영향을 받은 행 수가 0인 경우 UPDATE 실패로 간주한다.
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
record.changed(ADMIN.UPDATED_BY, admin.getUpdatedBy() != null); // required 'updated_by' column
record.changed(ADMIN.IS_DEL, admin.getIsDel() != null); // required 'is_del' column
return record.update();
```

### 6.4 Request DTO Class
- Controller에서 Parameter를 받아서 Service로 전달하는데 사용한다.
  - 해당 클래스는 Service Layer까지만 사용되며, 이후 Layer에서는 사용되지 않는다.
  - 유지보수의 편의성을 위해 본 프로젝트에서는 Command 객체를 사용하지 않는다.
- Lombok Annotation `@Getter`,`@Setter`,`@NoArgsConstructor`를 사용한다.
- 입력 유효성 검사는 우선적으로 `javax/jakarta validation` Annotation을 사용한다.

### 6.5 Response DTO Class
- Service의 반환 결과를 Controller로 전달하며, Controller는 이를 최종적으로 클라이언트에 반환한다.
- Lombok Annotation `@Getter`,`@Builder`를 사용한다.
- 외부 노출 필드만 포함한다. (내부 식별자/상태 값 노출은 정책에 따른다.)
- 날짜/시간/숫자 형식은 프로젝트 전체 컨벤션을 따른다.

### 6.6 Entity Class
- Service에서 생성되어 Repository에 전달되어 사용되는 객체.
- Lombok Annotation `@Getter`,`@Builder`를 사용한다.

### 6.6 Result Class
- jOOQ Query 결과를 보유하는 객체.
- Lombok Annotation `@Getter`를 사용한다.
- 명시적으로 요청하지 않는 이상 **Setter Method의 사용은 금지한다**.

---

## 8. 코드 주석 규칙
### 8.1. 기존에 작성된 주석 보호
- 관련된 코드가 삭제되지 않거나, 주석 삭제를 명시하지 않는 한 기존 주석의 삭제는 절대 금지한다.

### 8.1. 주석 작성 언어
- 언어를 명시하지 않는 한 모든 주석은 **한국어(Korean)로 작성**한다.

### 8.2 의도 기반 주석
- 주석 작성시 다음을 설명해야 한다
  - 해당 로직이 존재하는 이유
  - 로직이 의존하는 가정
  - 함부로 변경해서는 안 되는 사항

### 8.3 권장 주석 키워드
- 적절한 경우 다음 접두사를 사용한다:
  - `WHY:`
  - `DO NOT:`
  - `ASSUMPTION:`
  - `BOUNDARY:`

---

## 9. 테스트 코드 가이드라인
### 9.1. Java Version
- 모든 테스트 코드는 **Java 21을 기반으로 작성 및 실행**되어야 한다.
  - Java 22+ API, 프리뷰 기능 또는 최신 언어 구문을 사용하지 않아야 한다.
- 프로젝트는 **로컬, CI 및 프로덕션 환경** 모두에서 Java 21을 대상으로 한다.
- 빌드 도구(Gradle/Maven)는 툴체인 또는 컴파일러 설정을 통해 Java 21을 강제 적용한다.

### 9.2. Mocking Framework 규칙
- Spring Boot Test Mocking에는 `@MockitoBean`을 사용한다.
  - **`@MockBean`은 deprecated 되었으므로 절대 사용 금지한다.**
- 가능한 경우 필드 주입보다 생성자 주입 + `@MockitoBean`을 우선 적용한다.

### 9.3. 테스트 전략 (통합 테스트 우선)
- 본 프로젝트는 **단위 테스트보다 통합 테스트를 우선시**한다.
- 테스트는 다음을 검증하기 위해 작성된다.
  - 구성 요소 간 상호작용
  - 실제 구성, 연결 및 동작
  - 실제 SQL 실행 및 트랜잭션 동작
- 단위 테스트는 선택적으로 작성될 수 있으나, **주요 테스트 전략이 아니다**.