# JWT Utilities / JWT 유틸리티

[ENG](#eng) | [KR](#kr)

## ENG

Extracts the original TokenUtils and its JsonUtils dependency. Uses the original JJWT API, not the replacement Spring Security verifier.

### Source and minimal changes

Kept the original methods and expired-claim result. Removed token/key/claim values from TokenUtils log messages. Corrected JWT header/payload decoding to Base64URL. Added JAXB for the original JJWT library on Java 17 and tests. Removed TokenVerifier and SampleToken.

| Original source | Extracted file |
|---|---|
| `ProxyApplication.java` | [DemoApplication.java](src/main/java/io/github/parkwoong/demo/DemoApplication.java) |
| `spec/util/JsonUtils.java` | [spec/util/JsonUtils.java](src/main/java/io/github/parkwoong/demo/spec/util/JsonUtils.java) |
| `spec/util/TokenUtils.java` | [spec/util/TokenUtils.java](src/main/java/io/github/parkwoong/demo/spec/util/TokenUtils.java) |

### Structure

```text
pom.xml
src/main/java/io/github/parkwoong/demo/DemoApplication.java
src/main/java/io/github/parkwoong/demo/spec/util/JsonUtils.java
src/main/java/io/github/parkwoong/demo/spec/util/TokenUtils.java
src/main/resources/application.properties
src/test/java/io/github/parkwoong/demo/TokenUtilsTest.java
```

### How it works

parseJWTClaims reads the algorithm, verifies a signature, and returns claims. The HS256 branch uses a Base64URL secret; the RSA branch uses an X.509 public key. decodePayload and getUserInfo read user/client data. Tests generate temporary keys for HS256 and RS256.

### Run and test

Use Java 17 and Maven 3.6.3+. Run from the repository root:

```sh
mvn clean verify
mvn spring-boot:run
```

The app starts without an HTTP server or an added console workflow. Use the tests under src/test for actual calls and results. Database modules initialize H2 at startup.

### Formatting

All Java files use google-java-format 1.24.0 with the AOSP style: four-space indentation, consistent line breaks, and organized imports.

### Original limits

Important original behavior: expired tokens return claims with expired=true instead of null. Non-null claims do not mean access is allowed. decodePayload does not verify signatures. The original method requires both key strings even when one branch uses only one. Issuer/audience checks are not added. Use this to study the original code, not as a production authentication policy.

The build keeps Spring Boot 2.6.6 and the original MyBatis/JJWT APIs for source fidelity. Lombok 1.18.36 supports the local Java 17 build. These are legacy dependencies; this extraction is for learning, not a production-ready upgrade. No production integration or load test is included.

## KR

원본 TokenUtils와 의존하는 JsonUtils를 추출했습니다. 대체 구현인 Spring Security 검증기 대신 원본 JJWT API를 사용합니다.

### 원본과 최소 변경 사항

원본 메서드와 만료 클레임 반환 동작을 유지했습니다. TokenUtils 로그에서 토큰·키·클레임 값을 제거하고 JWT 헤더·페이로드 디코더를 Base64URL로 수정했습니다. Java 17에서 원본 JJWT를 실행하기 위한 JAXB와 테스트를 추가했습니다. TokenVerifier와 SampleToken은 제거했습니다.

| 원본 파일 | 추출 파일 |
|---|---|
| `ProxyApplication.java` | [DemoApplication.java](src/main/java/io/github/parkwoong/demo/DemoApplication.java) |
| `spec/util/JsonUtils.java` | [spec/util/JsonUtils.java](src/main/java/io/github/parkwoong/demo/spec/util/JsonUtils.java) |
| `spec/util/TokenUtils.java` | [spec/util/TokenUtils.java](src/main/java/io/github/parkwoong/demo/spec/util/TokenUtils.java) |

### 프로젝트 구조

```text
pom.xml
src/main/java/io/github/parkwoong/demo/DemoApplication.java
src/main/java/io/github/parkwoong/demo/spec/util/JsonUtils.java
src/main/java/io/github/parkwoong/demo/spec/util/TokenUtils.java
src/main/resources/application.properties
src/test/java/io/github/parkwoong/demo/TokenUtilsTest.java
```

### 코드 설명

parseJWTClaims는 알고리즘을 읽고 서명을 검증한 뒤 클레임을 반환합니다. HS256 분기는 Base64URL 비밀키, RSA 분기는 X.509 공개키를 사용합니다. decodePayload와 getUserInfo는 사용자·클라이언트 정보를 읽습니다. 테스트에서는 HS256·RS256용 임시 키를 생성합니다.

### 실행과 테스트

Java 17, Maven 3.6.3 이상을 사용합니다. 저장소 루트에서 실행하세요:

```sh
mvn clean verify
mvn spring-boot:run
```

앱은 HTTP 서버나 별도 콘솔 시나리오 없이 시작합니다. 실제 호출과 결과 확인은 src/test의 테스트를 이용하세요. DB 모듈은 시작 시 H2를 초기화합니다.

### 코드 포맷

전체 Java 파일에 google-java-format 1.24.0의 AOSP 스타일을 적용했습니다. 들여쓰기 4칸, 일관된 줄바꿈 및 import 정리를 사용합니다.

### 유지한 원본 동작과 제한

중요한 원본 동작: 만료 토큰은 null 대신 expired=true인 클레임을 반환합니다. 클레임이 존재한다고 접근을 허용하면 안 됩니다. decodePayload는 서명을 검증하지 않습니다. 원본 메서드는 한 분기에서 키 하나만 사용하더라도 두 키 문자열을 모두 요구합니다. issuer/audience 검증은 추가하지 않았습니다. 원본 학습용이며 운영 인증 정책으로 바로 사용하지 마세요.

원본 코드 보존을 위해 Spring Boot 2.6.6과 원본 MyBatis/JJWT API를 사용합니다. 로컬 Java 17 빌드를 위해 Lombok 1.18.36을 적용했습니다. 레거시 의존성을 사용하는 학습용 추출본이며 운영용 업그레이드가 아닙니다. 운영 연동 및 부하 테스트는 포함하지 않습니다.
