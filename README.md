# LTALK-SERVER-v2

## 📌 소개
`LTALK-SERVER-v2`는 실시간 채팅 및 음성 채팅 기능을 제공하는 **L-TALK 프로젝트의 메인 서버**입니다.  
비동기 TCP/IP 통신 기반으로 클라이언트와 서버 간 안정적인 연결을 유지하며,  
음성 채팅 서버인 **L-TALK-VOICESERVER**와의 중계를 지원합니다.

---

## 🛠️ 주요 기능

### ✅ NIO 기반 비동기 TCP/IP 통신
- `AsynchronousChannelGroup`을 이용해 **논블로킹 비동기 방식**으로 클라이언트와 통신
- 클라이언트와 L-TALK-VOICESERVER 간의 연결을 서버가 중계
- 클라이언트와 VOICESERVER 모두 동일한 프로토콜 기반 통신

### ✅ 음성 채팅 서버와의 연동
- **L-TALK-VOICESERVER**와 TCP를 통해 직접 통신
- 클라이언트가 음성 채팅방에 접속 요청 시 VOICESERVER에 채팅방 정보 및 클라이언트 정보를 전달
- VOICESERVER 내부에서 `chatRoomId`, `memberId`, `SocketAddress`를 기반으로 음성 채팅방 구성

### ✅ 사용자 인증 및 데이터 관리
- 회원가입, 로그인, 사용자 정보 관리
- 채팅방 생성, 참여, 메시지 전송, 읽음 처리 등 채팅 전반 기능 제공
- 클라이언트 접속 정보 및 채팅방 상태 관리

### ✅ 데이터베이스
- **MySQL**을 사용한 관계형 데이터베이스 구성
- **JPA**를 이용하여 Entity 관리 및 ORM 처리
- 주요 엔티티:
  - `Member`: 사용자 정보
  - `ChatRoom`: 채팅방 정보
  - `ChatRoomMember`: 채팅방 참여자 정보
  - `Chat`: 채팅 메시지


---

## ⚡ 주요 통신 흐름

1. 클라이언트 → 메인 서버: TCP/IP 접속 및 로그인/회원가입
2. 클라이언트 → 메인 서버: 음성 채팅방 접속 요청
3. 메인 서버 → VOICESERVER: 클라이언트의 `chatRoomId`, `memberId`, `ip`, `port` 전달
4. VOICESERVER: 음성 채팅방 구성 (`Map<chatRoomId, List<Member>>` 형태로 관리)
5. 클라이언트 ↔ 메인 서버 ↔ VOICESERVER: 상태 관리

---

## 🚀 사용 기술

| 기술 스택        | 설명                                 |
| ---------------- | ------------------------------------ |
| Java (NIO)       | 비동기 TCP 서버 (`AsynchronousChannelGroup`) |
| MySQL            | 사용자 및 채팅 데이터 관리          |
| JPA / Hibernate  | ORM 및 데이터베이스 연동             |
| JSON             | 클라이언트-서버 간 데이터 포맷      |

---

## 📌 개발 중 참고한 이슈들

- `DatagramSocket`을 이용한 UDP 포트 할당 시 `getLocalPort()` 사용
- 클라이언트와 VOICESERVER 간 연결 정보 전달 구조 (`chatRoomId`, `memberId`, `ip`, `port`)
- TCP와 UDP를 병행한 실시간 메시지 및 음성 데이터 처리 방식 고민
- 멀티스레딩 환경에서 안전한 사용자 접속 관리 및 채팅방 동기화

---

## 💬 향후 개발 계획

- 채팅방 메시지 페이징 처리 및 정렬 최적화
- 음성 채팅 패킷 지연 처리 및 패킷 손실 대응
- WebSocket 지원 검토 (확장성 확보를 위해)
- 클라이언트 인증 토큰 처리 방식 개선 (JWT 도입 검토)

---

## 📧 Contact

- 개발자: JaeHyung Lee  
- 프로젝트: L-TALK

