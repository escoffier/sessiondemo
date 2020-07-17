FROM openjdk:8-jdk-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
COPY docker-entrypoint.sh /usr/local/bin/
RUN export
#ENTRYPOINT ["java","-cp","app:app/lib/*", "com.example.sessiondemo.SessiondemoApplication"]
ENTRYPOINT ["docker-entrypoint.sh"]