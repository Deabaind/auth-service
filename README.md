<!-- 프로젝트 이름 -->
Auth-Service
===
:large_blue_diamond: 개요
---
<!-- 프로젝트의 목표가 무엇인가 -->
**목표**
- 기능 구현
- 기능 테스트
- Swagger를 통한 API 명세서 구현
- AWS EC2 인스턴스를 통한 배포

**개발 환경**
- Oracle JDK 17.0.14<br>
- IDE: IntelliJ<br>
- Framework: Spring<br>

- - -
## :large_blue_diamond: 요구사항
- 기능 구현
  - 사용자 인증 시스템 구축해야 합니다.
  - JWT 기반 인증 메커니즘을 구현하여 보안성 강화해야 합니다.
  - 역활(Role) 기반 접근 제어를 적용하여 관리자(Admin) 권한이 필요한 API 보호해야 합니다.<br>

- 기능 테스트
  - 각 기능 구현을 확인해야합니다.<br>

- API 명세서 구현
  - Swagger 등 스펙을 기반으로 API 문서화 도구를 프로젝트에 추가해야 합니다.
  - 각 API에 대한 설명, 파라미터, 요청/응답 예시 등을 Swagger UI에 등록하여 브라우저에서 쉽게 확인할 수 있어야 합니다.<br>

- AWS EC2를 활용한 배포
  - AWS EC2 인스턴스 생성 후 기본 환경 설정을 합니다.
  - 두 가지 방법 중 하나로 애플리케이션을 배포합니다.
    - 방법 1: 소스 코드를 EC2로 가져와서 빌드
    - 방법 2: 빌드된 JAR 파일 직접 업로드
  - 애플리케이션을 실행하고 외부에서 접근 가능하도록 구성합니다.
  - (선택사항) Nginx를 리버스 프록시로 설정하여 요청을 애플리케이션으로 전달합니다.

- - -
## :large_blue_diamond: API 명세서 & ERD 작성
### [:memo: API 명세서](http://16.176.143.39:8080/swagger-ui/index.html)
### :memo: ERD
![](https://www.notion.so/image/attachment%3A50857b67-9d25-4d78-b86b-72e2436894a6%3Aimage.png?table=block&id=23d29343-d978-8080-a8d4-d4ff44a62368&spaceId=234c0de8-f981-4889-9307-1dd0a0805892&width=1420&userId=&cache=v2)


- - -
## :large_blue_diamond: 구현된 기능
- API
  - 회원 가입 API
  - 로그인 API
  - 권한 부여 API<br>

- 테스트
  - 통합 테스트 구현: h2 DB를 사용한 통합 테스트 구현
  - 예외 처리 테스트: 비니스 로직 예외 상황에 대한 검증 구현
  - 데이터 무결성: @Transactional과 @Rollback을 통한 테스트 격리 보장

- Swagger API
  - API 문서화: 모든 API 엔드 포인트에 대한 상세한 문서화 완료
  - 요청/응답 예시: 각 API의 성공/실패 케이스별 JSON 예시 제공
  - 스키마 정의: DTO 클래스에 대한 명확한 스키마 및 유효성 검증 규칭 명시
  - 토큰 요청: JWT 토큰이 필요한 API에 대한 표시

- AWS EC2 배포
  - Amazon Linux 2023 기반 EC2 인스턴스 생성 및 서버 구성
  - Git 클론 방식을 통한 소스코드 배포 및 Gradle 빌드
  - 외부 접근 가능한 실제 운영 환경 구축 (http://16.176.143.39:8080)
  - API 테스트: Postman을 통한 클라우드 서버 API 검증 완료

- - -
## :large_blue_diamond: 구현 예시
**회원가입 성공**
![](https://www.notion.so/image/attachment%3Ab35194a4-9363-468f-8306-23e433f79ffa%3A43cef1b0-0fe3-4b3a-9f34-16db19e22b23.png?table=block&id=23d29343-d978-8055-9ca6-f9d8b583b2d8&spaceId=234c0de8-f981-4889-9307-1dd0a0805892&width=1420&userId=&cache=v2)
<br><br>
**로그인 성공**
![](https://www.notion.so/image/attachment%3Aa849feab-5729-404d-a8c5-66ff7ab2b989%3A0ab0da7c-2b08-4d59-ab96-81af677d5c49.png?table=block&id=23d29343-d978-8013-ba48-ec2af09ff513&spaceId=234c0de8-f981-4889-9307-1dd0a0805892&width=1420&userId=&cache=v2)
<br><br>
**권한 부여 성공**
![](https://www.notion.so/image/attachment%3A406aa2b0-1c70-4e46-bf9c-2848742bdc35%3A9f3756dd-945c-49f4-9719-c201025cd95a.png?table=block&id=23d29343-d978-80cb-a3f6-d570ece24514&spaceId=234c0de8-f981-4889-9307-1dd0a0805892&width=1420&userId=&cache=v2)
