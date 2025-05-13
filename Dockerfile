# 빌드 스테이지: Java와 Gradle을 사용하여 애플리케이션 빌드
FROM gradle:8.5-jdk21 AS build

WORKDIR /app

# 먼저 의존성만 복사하고 캐싱
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY gradlew ./
RUN chmod +x ./gradlew

# 이 명령은 의존성을 다운로드하고 캐싱합니다
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src ./src

# 애플리케이션 빌드
RUN ./gradlew build -x test --no-daemon

# 실행 스테이지: 빌드된 JAR를 실행할 경량 이미지
FROM openjdk:21-slim

WORKDIR /app

# 타임존 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 빌드 스테이지에서 JAR 파일 복사
COPY --from=build /app/build/libs/board-0.0.1-SNAPSHOT.jar app.jar

# 컨테이너 실행 시 JAR 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]