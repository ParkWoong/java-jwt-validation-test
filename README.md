# JWT Utilities / JWT 유틸리티

[ENG](#eng) | [KR](#kr)

## ENG

### Project structure

```text
pom.xml
src/main/java/io/github/parkwoong/demo/DemoApplication.java
src/main/java/io/github/parkwoong/demo/JwtController.java
src/main/java/io/github/parkwoong/demo/spec/util/JsonUtils.java
src/main/java/io/github/parkwoong/demo/spec/util/TokenUtils.java
src/main/resources/application.properties
src/test/java/io/github/parkwoong/demo/TokenUtilsTest.java
```

### Code explanation

`TokenUtils.parseJWTClaims` reads the token algorithm and verifies the signature with JJWT. HS256 uses a Base64URL secret; the RSA branch uses a Base64-encoded X.509 public key. `decodePayload` reads payload text without verifying a signature; `getUserInfo` reads user/client fields through `JsonUtils`.

An expired token returns claims with `expired=true`. Non-null claims alone do not mean access is allowed. `isNotExpired` and `isValidType` provide separate checks. Both key strings must be non-empty, even when the selected branch uses only one. Issuer and audience are not checked.

JwtController calls TokenUtils and returns the validation result and subject without echoing the token or key.

### Run and test

Use JDK 8 and Maven 3.6.3+. Set JAVA_HOME to your JDK 8 directory, then run from the repository root:

```sh
java -version
mvn -version
mvn clean verify
mvn spring-boot:run
```

`TokenUtilsTest` creates temporary HS256/RSA keys and checks valid signatures, expiration, malformed input, and a wrong key.

The app listens on localhost:8080. The original unit tests still check the underlying methods.

#### Postman API

Send POST http://localhost:8080/api/jwt/validate with Content-Type: application/json and this public sample body:

    {"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzYW1wbGUtdXNlciIsImV4cCI6MjA1MDAwMDAwMH0.CScMgIoDX98T6FysUejofuWdKGMs5qW8UEdSfUYWajw","rsaPublicKey":"unused","hs256Secret":"c2FtcGxlLXNlY3JldC1mb3ItcG9zdG1hbg"}

Expected: HTTP 200, {"valid":true,"subject":"sample-user"}. The secret is a sample Base64URL value. A bad token returns {"valid":false,"subject":null}; never send a real token to this sample endpoint.

## KR

### 프로젝트 구조

```text
pom.xml
src/main/java/io/github/parkwoong/demo/DemoApplication.java
src/main/java/io/github/parkwoong/demo/JwtController.java
src/main/java/io/github/parkwoong/demo/spec/util/JsonUtils.java
src/main/java/io/github/parkwoong/demo/spec/util/TokenUtils.java
src/main/resources/application.properties
src/test/java/io/github/parkwoong/demo/TokenUtilsTest.java
```

### 코드 설명

`TokenUtils.parseJWTClaims`는 토큰의 알고리즘을 읽고 JJWT로 서명을 검증합니다. HS256은 Base64URL 비밀키를, RSA 분기는 Base64로 인코딩한 X.509 공개키를 사용합니다. `decodePayload`는 서명 검증 없이 페이로드를 읽고, `getUserInfo`는 `JsonUtils`로 사용자·클라이언트 필드를 읽습니다.

만료 토큰도 `expired=true`인 클레임을 반환하므로 값이 존재한다고 접근을 허용하면 안 됩니다. `isNotExpired`와 `isValidType`으로 만료·유형을 별도로 확인합니다. 선택한 분기에서 키 하나만 사용하더라도 두 키 문자열이 모두 필요합니다. issuer와 audience는 검증하지 않습니다.

JwtController는 TokenUtils를 호출하고 토큰이나 키를 다시 출력하지 않은 채 검증 결과와 subject를 반환합니다.

### 실행과 테스트

JDK 8과 Maven 3.6.3 이상을 사용합니다. JAVA_HOME을 JDK 8 경로로 설정하고 저장소 루트에서 실행하세요:

```sh
java -version
mvn -version
mvn clean verify
mvn spring-boot:run
```

`TokenUtilsTest`는 임시 HS256/RSA 키로 정상 서명, 만료, 잘못된 형식 및 잘못된 키를 확인합니다.

앱은 localhost:8080에서 실행됩니다. 기존 단위 테스트는 내부 메서드를 계속 확인합니다.

#### Postman API

Content-Type: application/json으로 POST http://localhost:8080/api/jwt/validate에 공개 샘플 본문을 보내세요.

    {"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzYW1wbGUtdXNlciIsImV4cCI6MjA1MDAwMDAwMH0.CScMgIoDX98T6FysUejofuWdKGMs5qW8UEdSfUYWajw","rsaPublicKey":"unused","hs256Secret":"c2FtcGxlLXNlY3JldC1mb3ItcG9zdG1hbg"}

예상 결과: HTTP 200, {"valid":true,"subject":"sample-user"}. secret은 샘플 Base64URL 값입니다. 잘못된 토큰은 {"valid":false,"subject":null}을 반환합니다. 실제 토큰은 이 예제 API에 보내지 마세요.
