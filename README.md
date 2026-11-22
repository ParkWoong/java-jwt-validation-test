# JWT Validation / JWT 서명·클레임 검증

[ENG](#eng) | [KR](#kr)

## ENG

Validates JWT signatures with HS256 or RS256 using Spring Security and Nimbus. The caller selects the algorithm and provides the key.

### Project structure

```text
pom.xml
src/
├── main/
│   └── java/io/github/parkwoong/demo/
│       ├── DemoApplication.java
│       ├── TokenVerifier.java
│       └── SampleToken.java
└── test/java/io/github/parkwoong/demo/
    └── TokenVerifierTest.java
```

Java package: `io.github.parkwoong.demo`.

| File | Role |
|---|---|
| [DemoApplication.java](src/main/java/io/github/parkwoong/demo/DemoApplication.java) | Starts Spring Boot and runs the example. |
| [TokenVerifier.java](src/main/java/io/github/parkwoong/demo/TokenVerifier.java) | Builds HS256 and RS256 decoders and sets claim validation rules. |
| [SampleToken.java](src/main/java/io/github/parkwoong/demo/SampleToken.java) | Signs a sample HS256 token for the console demo. |
| [pom.xml](pom.xml) | Dependencies and build settings. |

### How it works

DemoApplication generates a temporary 256-bit HMAC key, signs a token, verifies it, and prints sample-user. The verifier checks issuer demo-issuer, audience demo-api, a non-empty subject, an expiration time, and the default timestamp rules. RS256 is shown in the tests.

### Run

Requirements: Java 17+ and Maven 3.6.3+. Run these commands from the repository root.

```sh
mvn clean verify
mvn spring-boot:run
```

No token or key is needed before running. Keys are created in memory and are not saved. This is a console demo.

### Tests

TokenVerifierTest checks valid HS256 and RS256 tokens. It also checks an expired token, a wrong HMAC key, malformed input, and missing required claims.

[TokenVerifierTest.java](src/test/java/io/github/parkwoong/demo/TokenVerifierTest.java)

### Scope and limits

Issuer and audience are fixed demo values. Timestamp validation uses the library’s default clock tolerance. This is not a login service and does not include key storage, key rotation, or HTTP security rules.

This learning example uses Spring Boot 3.5.16. Before production use, move to a supported version; see the [3.5 OSS support notice](https://spring.io/blog/2026/06/25/spring-boot-3-5-16-available-now/). Production connections and load tests are not included.

Adapted from a Java proxy project using generic names and sample data. Original Git history, production settings, and business identifiers are not included.

## KR

Spring Security와 Nimbus로 HS256 또는 RS256 JWT 서명을 검증합니다. 호출자가 알고리즘을 선택하고 키를 제공합니다.

### 프로젝트 구조

```text
pom.xml
src/
├── main/
│   └── java/io/github/parkwoong/demo/
│       ├── DemoApplication.java
│       ├── TokenVerifier.java
│       └── SampleToken.java
└── test/java/io/github/parkwoong/demo/
    └── TokenVerifierTest.java
```

Java 패키지: `io.github.parkwoong.demo`.

| 파일 | 역할 |
|---|---|
| [DemoApplication.java](src/main/java/io/github/parkwoong/demo/DemoApplication.java) | Spring Boot를 시작하고 예제를 실행합니다. |
| [TokenVerifier.java](src/main/java/io/github/parkwoong/demo/TokenVerifier.java) | HS256·RS256 디코더를 만들고 클레임 검증 규칙을 설정합니다. |
| [SampleToken.java](src/main/java/io/github/parkwoong/demo/SampleToken.java) | 콘솔 실습용 HS256 토큰을 생성하고 서명합니다. |
| [pom.xml](pom.xml) | 의존성과 빌드 설정입니다. |

### 동작 방식

DemoApplication이 임시 256비트 HMAC 키를 생성하고 토큰을 서명·검증한 뒤 sample-user를 출력합니다. 검증기는 issuer=demo-issuer, audience=demo-api, 비어 있지 않은 subject, 만료 시각 및 기본 시간 검증 규칙을 확인합니다. RS256 예제는 테스트에 있습니다.

### 실행 방법

Java 17 이상, Maven 3.6.3 이상이 필요합니다. 저장소 루트에서 실행하세요.

```sh
mvn clean verify
mvn spring-boot:run
```

실행 전에 토큰이나 키를 준비할 필요가 없습니다. 키는 메모리에서 생성하며 저장하지 않습니다. 콘솔에서 실행되는 예제입니다.

### 테스트

TokenVerifierTest는 정상 HS256·RS256 토큰과 만료 토큰, 잘못된 HMAC 키, 잘못된 형식, 필수 클레임 누락을 확인합니다.

[TokenVerifierTest.java](src/test/java/io/github/parkwoong/demo/TokenVerifierTest.java)

### 범위와 제한

issuer와 audience는 예제용 고정 값입니다. 시간 검증에는 라이브러리의 기본 시계 오차 허용 범위가 적용됩니다. 로그인 서비스가 아니며 키 저장·교체 및 HTTP 접근 제어는 포함하지 않습니다.

Spring Boot 3.5.16 기반 학습용 예제입니다. 운영 도입 전 지원 버전으로 전환하세요. [3.5 OSS 지원 종료 안내](https://spring.io/blog/2026/06/25/spring-boot-3-5-16-available-now/)를 참고할 수 있습니다. 운영 시스템 연결 및 부하 테스트는 포함하지 않습니다.

기존 Java 프록시 프로젝트에서 기술을 추출해 범용 이름과 샘플 데이터로 재작성했습니다. 기존 Git 이력, 운영 설정 및 업무 식별자는 포함하지 않습니다.
