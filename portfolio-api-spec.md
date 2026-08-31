# 포트폴리오 사이트 API 명세서

## 개요

- Base URL: `/api`
- 인증 방식: JWT (관리자 로그인 시 발급, `Authorization: Bearer {token}` 헤더로 전달)
- FO(방문자용) 엔드포인트는 인증 불필요, BO(관리자용) 엔드포인트는 전부 인증 필요
- 관리자 전용 경로는 `/api/admin/**` 하위로 분리

---

## 1. 인증 (Auth)

| Method | Endpoint | 설명 | 인증 |
|---|---|---|---|
| POST | `/api/auth/login` | 관리자 로그인, 성공 시 JWT 발급 | 불필요 |
| POST | `/api/auth/logout` | 로그아웃 (클라이언트 토큰 폐기 안내) | 필요 |

**POST /api/auth/login**
```json
// Request
{ "username": "admin", "password": "..." }

// Response 200
{ "accessToken": "eyJ...", "expiresIn": 3600 }
```

---

## 2. 프로젝트 — FO (공개)

| Method | Endpoint | 설명 | 인증 |
|---|---|---|---|
| GET | `/api/projects` | 프로젝트 목록 (메인 화면 카드용) | 불필요 |
| GET | `/api/projects/{id}` | 프로젝트 상세 (기능, 트러블슈팅, 기술스택 포함) | 불필요 |

**GET /api/projects/{id}** 응답 예시
```json
{
  "id": 1,
  "title": "중고거래 플랫폼",
  "summary": "...",
  "thumbnailUrl": "https://.../thumb.jpg",
  "githubUrl": "https://github.com/...",
  "demoUrl": "https://...",
  "startDate": "2026-03-01",
  "endDate": "2026-05-01",
  "techStacks": [{ "id": 1, "name": "Spring Boot", "category": "Backend" }],
  "features": [
    {
      "id": 10,
      "name": "실시간 알림",
      "imageUrl": "https://.../feature1.jpg",
      "description": "...",
      "reason": "...",
      "troubleshooting": {
        "problem": "...", "analysis": "...", "action": "...", "result": "..."
      }
    },
    {
      "id": 11,
      "name": "다크모드 지원",
      "imageUrl": "https://.../feature2.jpg",
      "description": "...",
      "reason": "...",
      "troubleshooting": null
    }
  ]
}
```
`troubleshooting`이 `null`이면 FO에서 해당 기능 블록에 트러블슈팅 카드를 렌더링하지 않음.

---

## 3. 프로젝트 — BO (관리자)

| Method | Endpoint | 설명 | 인증 |
|---|---|---|---|
| GET | `/api/admin/projects` | 관리용 프로젝트 목록 | 필요 |
| POST | `/api/admin/projects` | 프로젝트 생성 | 필요 |
| PUT | `/api/admin/projects/{id}` | 프로젝트 수정 | 필요 |
| DELETE | `/api/admin/projects/{id}` | 프로젝트 삭제 | 필요 |

**POST /api/admin/projects**
```json
// Request
{
  "title": "...", "summary": "...", "thumbnailUrl": "...",
  "githubUrl": "...", "demoUrl": "...",
  "startDate": "2026-03-01", "endDate": "2026-05-01",
  "techStackIds": [1, 2, 3]
}
```

---

## 4. 기능 (Feature) — BO

| Method | Endpoint | 설명 | 인증 |
|---|---|---|---|
| POST | `/api/admin/projects/{projectId}/features` | 기능 추가 | 필요 |
| PUT | `/api/admin/features/{id}` | 기능 수정 | 필요 |
| DELETE | `/api/admin/features/{id}` | 기능 삭제 | 필요 |

```json
// Request
{ "name": "실시간 알림", "imageUrl": "...", "description": "...", "reason": "...", "sortOrder": 1 }
```

---

## 5. 트러블슈팅 — BO

| Method | Endpoint | 설명 | 인증 |
|---|---|---|---|
| POST | `/api/admin/features/{featureId}/troubleshootings` | 트러블슈팅 추가 | 필요 |
| PUT | `/api/admin/troubleshootings/{id}` | 트러블슈팅 수정 | 필요 |
| DELETE | `/api/admin/troubleshootings/{id}` | 트러블슈팅 삭제 | 필요 |

```json
// Request
{ "problem": "...", "analysis": "...", "action": "...", "result": "..." }
```

---

## 6. 기술 스택 (Tech Stack)

| Method | Endpoint | 설명 | 인증 |
|---|---|---|---|
| GET | `/api/tech-stacks` | 공개 기술 스택 목록 (메인 화면 하단용) | 불필요 |
| GET | `/api/admin/tech-stacks` | 관리용 기술 스택 목록 | 필요 |
| POST | `/api/admin/tech-stacks` | 기술 스택 추가 | 필요 |
| DELETE | `/api/admin/tech-stacks/{id}` | 기술 스택 삭제 | 필요 |

```json
// POST Request
{ "name": "Redis", "category": "Backend" }
```

**삭제 정책**: 삭제하려는 스택이 하나 이상의 프로젝트에서 참조 중이면 `409 Conflict`와 함께
`{ "message": "사용 중인 프로젝트가 있어 삭제할 수 없습니다.", "usedByProjectIds": [1, 3] }` 반환.

---

## 7. 이미지 업로드

| Method | Endpoint | 설명 | 인증 |
|---|---|---|---|
| POST | `/api/admin/images` | 이미지 업로드 (S3/스토리지 저장 후 URL 반환) | 필요 |

```json
// Request: multipart/form-data (file)

// Response 200
{ "imageUrl": "https://.../uploads/abc123.jpg" }
```
프로젝트 대표 이미지, 기능별 이미지 모두 이 엔드포인트로 먼저 업로드해 URL을 받은 뒤,
그 URL을 프로젝트/기능 생성·수정 요청 바디에 담아 전송.

---

## 상태 코드 규칙

| 코드 | 의미 |
|---|---|
| 200 | 성공 |
| 201 | 생성 성공 |
| 400 | 요청 값 오류 |
| 401 | 인증 필요 (토큰 없음/만료) |
| 403 | 권한 없음 |
| 404 | 리소스 없음 |
| 409 | 참조 무결성 충돌 (기술 스택 삭제 등) |
