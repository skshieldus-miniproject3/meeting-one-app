## Meeting One Line (Frontend + Backend + AI)

AI 기반 회의 전사·요약·검색을 위한 풀스택 프로젝트입니다.
브라우저에서 음성을 녹음/업로드하면, 백엔드가 파일을 관리하고
AI 서버가 STT와 다양한 분석을 수행한 뒤 결과를 콜백으로 넘겨 전체 회의 기록을 완성합니다.

---

## 실행방법

1. docker pull mariadb:11 (이미지 없을 경우)
2. docker-compose up -d

---

## 📁 프로젝트 구조

src/main/java/com/meetingoneline/meeting_one_line
┣ global
┃ ┣ config
┃ ┣ dto
┃ ┣ exception
┃ ┗ jwt
┣ user
┣ auth
┗ meeting
┣ meeting
┣ feedback
┣ controller
┣ service
┣ dto
┣ entity
┗ repository

---

## 🚀 백엔드 메인 기술 스택

| 영역                      | 기술                           |
| ------------------------- | ------------------------------ |
| **Language**              | Java 17                        |
| **Framework**             | Spring Boot 3.x                |
| **ORM / DB**              | Spring Data JPA + MariaDB      |
| **Security**              | Spring Security + JWT          |
| **API 문서화**            | Springdoc OpenAPI (Swagger UI) |
| **빌드 도구**             | Gradle                         |
| **로깅**                  | SLF4J                          |
| **테스트 DB / 로컬 개발** | Docker + MariaDB:11.4          |

---

### 핵심 기능

- 음성 녹음/업로드 및 대용량 파일 업로드(최대 1GB 설정)
- STT(화자 분리), 요약, 키워드, 액션아이템, 감정/주제 분석, 회의록 생성
- 임베딩 기반 의미 검색(사용자별 데이터 분리)
- 회의록 목록/상세/다운로드(txt/md/json)
- JWT 인증 및 토큰 자동 갱신(프론트 클라이언트 포함)

---

### 각 기술 스택 요약

- Frontend: Next.js 15(App Router), React 19, TypeScript, Tailwind CSS, shadcn/ui
- Backend: Java 17, Spring Boot 3, Spring Data JPA, MariaDB, Springdoc(OpenAPI)
- AI Server: FastAPI, LangChain, OpenAI, NCP CLOVA Speech STT

---

### 시스템 아키텍처 및 흐름

1. 사용자가 `Frontend`에서 음성 녹음 또는 파일 업로드
2. `Backend`가 파일을 저장(`/uploads/meetings`)하고 메타데이터/상태 관리
3. `Backend → AI Server`로 `/ai/analyze` 요청(meetingId, filePath, userId, meetingTitle)

4. `AI Server`가 STT 및 모든 AI 분석을 수행

- 요약(summary), 키워드(keywords)
- 액션아이템(action items)
- 회의록(meeting notes)
- 감정 분석(sentiment)
- 주제 분류(topics)
- 후속 질문(follow-up questions)

5. `Backend`의 콜백 URL(`/api/meetings/{id}/callback`)로 결과 전송
6. `Frontend`는 상태 폴링/갱신으로 분석 완료 시 결과 화면 노출 및 다운로드 제공

---
