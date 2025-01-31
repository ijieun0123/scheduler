# Scheduler &#x20;

## 프로젝트 소개

사용자가 일정(Schedule)을 생성, 수정, 삭제할 수 있는 RESTful API입니다. 일정은 특정 사용자(User)가 작성하며, 사용자 이메일은 중복될 수 없습니다.

## 기술 스택

- **언어:** Java 17
- **프레임워크:** Spring Boot 3
- **데이터베이스:** MySQL
- **빌드 툴:** Gradle
- **라이브러리:** Spring JDBC, Lombok, Spring Web

## ERD 설계
![Image](https://github.com/user-attachments/assets/e5dddfcf-69a2-4682-be85-df283cd42498)

## API 명세

### 1. 일정 생성 API

**[POST] /api/schedule**

```json
{
    "user": {
        "name": "홍길동",
        "email": "hong@example.com"
    },
    "todo": "회의 일정 잡기",
    "password": "1234"
}
```

#### 응답 예시

```json
{
    "id": 1,
    "userId": 1,
    "todo": "회의 일정 잡기",
    "createdAt": "2025-01-26T19:26:30",
    "updatedAt": "2025-01-26T19:26:30",
    "user": {
        "id": 1,
        "name": "홍길동",
        "email": "hong@example.com",
        "createdAt": "2025-01-26T19:26:30",
        "updatedAt": "2025-01-26T19:26:30"
    }
}
```

### 2. 일정 수정 API

**[PATCH] /api/schedule/{id}**

```json
{
    "password": "1234",
    "todo": "회의 일정 변경",
    "user": {
        "name": "홍길동 수정"
    }
}
```

#### 응답 예시

```json
{
    "id": 1,
    "userId": 1,
    "todo": "회의 일정 변경",
    "createdAt": "2025-01-26T19:26:30",
    "updatedAt": "2025-01-27T10:00:00",
    "user": {
        "id": 1,
        "name": "홍길동 수정",
        "email": "hong@example.com",
        "createdAt": "2025-01-26T19:26:30",
        "updatedAt": "2025-01-27T10:00:00"
    }
}
```

### 3. 일정 삭제 API

**[DELETE] /api/schedule/{id}**

#### 응답 예시

```json
1
```

### 4. 일정 전체 조회 API (페이징 적용)

**[POST] /api/schedule/list**

```json
{
    "pageNumber": 1,
    "pageSize": 10
}
```

#### 응답 예시

```json
[
    {
        "id": 9,
        "userId": 1,
        "todo": "일정 수정",
        "createdAt": "2025-01-27T15:24:56",
        "updatedAt": "2025-01-27T15:26:34",
        "user": {
            "id": 1,
            "name": "이름 수정",
            "email": "john.doe@example.com",
            "createdAt": "2025-01-27T14:06:16",
            "updatedAt": "2025-01-27T15:26:34"
        }
    }
]
```

### 5. 일정 단건 조회 API

**[GET] /api/schedule/{id}**

#### 응답 예시

```json
{
    "id": 9,
    "userId": 1,
    "todo": "일정 생성 기능 추가 2",
    "createdAt": "2025-01-27T15:24:56",
    "updatedAt": "2025-01-27T15:24:56",
    "user": {
        "id": 1,
        "name": "John Doe",
        "email": "john.doe@example.com",
        "createdAt": "2025-01-27T14:06:16",
        "updatedAt": "2025-01-27T14:06:16"
    }
}
```

### 6. 일정 다건 조회 API (조건 검색)

**[GET] /api/schedule?updatedAt=2025-01-27&userId=1**

#### 응답 예시

```json
[
    {
        "id": 9,
        "userId": 1,
        "todo": "일정 수정",
        "createdAt": "2025-01-27T15:24:56",
        "updatedAt": "2025-01-27T15:26:34",
        "user": {
            "id": 1,
            "name": "이름 수정",
            "email": "john.doe@example.com",
            "createdAt": "2025-01-27T14:06:16",
            "updatedAt": "2025-01-27T15:26:34"
        }
    }
]
```

## 실행 방법

1. MySQL에서 데이터베이스 및 테이블을 생성합니다.
2. `application.yml`에서 DB 설정을 추가합니다.
3. 프로젝트를 빌드 및 실행합니다.

```bash
mvn clean install
mvn spring-boot:run
```

##

